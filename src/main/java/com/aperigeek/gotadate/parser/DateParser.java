/*
 * This file is part of gotadate.
 *
 * gotadate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * gotadate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with gotadate.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aperigeek.gotadate.parser;

import com.aperigeek.gotadate.parser.la.LookAheadTable;
import com.aperigeek.gotadate.token.DateTokenizer;
import com.aperigeek.gotadate.token.Token;
import com.aperigeek.gotadate.token.TokenType;
import com.aperigeek.gotadate.token.TokenizerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;

/**
 *
 * @author Vivien Barousse
 */
public class DateParser {

    private LookAheadTable next;

    private Token token;

    private DateTimeZone zone = DateTimeZone.getDefault();

    private DateTime now = DateTime.now(zone);

    private List<Date> parsed = new ArrayList<Date>();
    
    private Map<String, Integer> MONTHS_MAP = new HashMap<String, Integer>() {{
        put("January", 1);
        put("Jan", 1);
        put("February", 2);
        put("Feb", 2);
        put("March", 3);
        put("Mar", 3);
        put("April", 4);
        put("Apr", 4);
        put("May", 5);
        put("June", 6);
        put("Jun", 6);
        put("July", 7);
        put("Jul", 7);
        put("August", 8);
        put("Aug", 8);
        put("September", 9);
        put("Sept", 9);
        put("Sep", 9);
        put("October", 10);
        put("Oct", 10);
        put("November", 11);
        put("Nov", 11);
        put("December", 12);
        put("Dec", 12);
    }};

    public DateParser(DateTokenizer tokenizer) throws DateParseException {
        this.next = new LookAheadTable(tokenizer);
        next();
    }

    protected void next() throws DateParseException {
        try {
            token = next.pop();
        } catch (TokenizerException ex) {
            throw new DateParseException("Unable to read from source", ex);
        }
    }

    protected Token lookahead(int i) throws DateParseException {
        try {
            return next.get(i);
        } catch (TokenizerException ex) {
            throw new DateParseException("Unable to read from source", ex);
        }
    }

    public void parse() throws DateParseException {
        while (token != null) {
            LocalDate date = null;
            LocalTime time = null;
            try {
                if (isDate()) {
                    date = parseDate();
                }
                if (isTime()) {
                    time = parseTime();
                }
                if (date == null && isDate()) {
                    date = parseDate();
                }
            } catch (UnexpectedTokenException ex) {
            } finally {
                if (time != null) {
                    ReadableInstant ref =
                            (date == null) ? now : date.toDateMidnight();
                    DateTime dt = time.toDateTime(ref);
                    parsed.add(dt.toDate());
                } else if (date != null) {
                    parsed.add(date.toDate());
                }
            }
            next();
        }
    }

    /**
     * Parse a date and store it in the parsed dates list
     * 
     * A date is defined by the following syntax:
     * Date:
     *     number "/" number "/" number
     * 
     * The interpretation of this string depends on the value of each number.
     * The parser tries to determine which value is the day, the month and
     * the year.
     */
    protected LocalDate parseDate() throws DateParseException,
                                           UnexpectedTokenException {
        // Human-like strings
        if (isToken("yesterday")) {
            return now.minusDays(1).toLocalDate();
        } else if (isToken("tomorrow")) {
            return now.plusDays(1).toLocalDate();
        }
        
        int[] ls = new int[3];

        ls[0] = getInt();
        if (isToken('/')) {
            check('/');
            ls[1] = getInt();
            check('/');
            ls[2] = getInt();
        } else if (isMonthName(token)) {
            ls[1] = getMonth();
            ls[2] = getInt();
        } else {
            throw new UnexpectedTokenException();
        }

        if (ls[1] > 12 && ls[0] <= 12) {
            int tmp = ls[1];
            ls[1] = ls[0];
            ls[0] = tmp;
        }

        LocalDate date = new LocalDate(ls[2], ls[1], ls[0]);
        return date;
    }

    protected LocalTime parseTime() throws DateParseException,
                                           UnexpectedTokenException {
        int[] ls = new int[3];

        ls[0] = getInt();
        if (isToken(':')) {
            check(':');
            ls[1] = getInt();
        }
        if (isToken(':')) {
            check(':');
            ls[2] = getInt();
        }

        if (isToken("AM")) {
            next();
        } else if (isToken("PM")) {
            ls[0] = (ls[0] % 12) + 12;
            next();
        }

        LocalTime time = new LocalTime(ls[0], ls[1], ls[2]);
        return time;
    }

    protected boolean isDate() throws DateParseException {
        if (token == null) {
            return false;
        }
        
        // Human-like tokens
        if (isToken("yesterday") ||
                isToken("tomorrow")) {
            return true;
        }
        
        if (token.getType() != TokenType.NUMBER) {
            return false;
        }
        
        if (isToken('/', lookahead(0))) {
            return true;
        } else if (isMonthName(lookahead(0))) {
            return true;
        } else {
            return false;
        }
    }
    
    protected boolean isMonthName(Token t) {
        if (t != null && 
                t.getType() == TokenType.STRING &&
                MONTHS_MAP.containsKey(t.getValue())) {
            return true;
        }
        
        return false;
    }

    protected boolean isTime() throws DateParseException {
        if (token == null) {
            return false;
        }
        
        if (token.getType() != TokenType.NUMBER) {
            return false;
        }
        
        if (isToken(':', lookahead(0)) ||
                isToken("PM", lookahead(0)) ||
                isToken("AM", lookahead(0))) {
            return true;
        }
        
        return false;
    }

    protected boolean isToken(char ch) {
        return isToken(ch, token);
    }

    protected boolean isToken(char ch, Token token) {
        if (token == null || token.getType() != TokenType.SEPARATOR) {
            return false;
        }

        Token<Character> t = token;
        char actual = t.getValue();
        if (actual != ch) {
            return false;
        }

        return true;
    }

    protected boolean isToken(String s) {
        return isToken(s, token);
    }

    protected boolean isToken(String s, Token token) {
        if (token == null || token.getType() != TokenType.STRING) {
            return false;
        }

        Token<String> t = token;
        String actual = t.getValue();
        if (!actual.equals(s)) {
            return false;
        }

        return true;
    }

    protected void check(char ch) throws UnexpectedTokenException,
                                         DateParseException {
        doCheckType(TokenType.SEPARATOR);

        Character chVal = (Character) token.getValue();
        if (ch != chVal) {
            throw new UnexpectedTokenException();
        }
        next();
    }

    protected void checkType(TokenType type) throws DateParseException,
                                                    UnexpectedTokenException {
        doCheckType(type);
        next();
    }

    protected void doCheckType(TokenType type)
            throws UnexpectedTokenException {
        if (token == null || token.getType() != type) {
            throw new UnexpectedTokenException();
        }
    }

    protected int getInt() throws DateParseException,
                                  UnexpectedTokenException {
        doCheckType(TokenType.NUMBER);

        Number nbValue = (Number) token.getValue();
        next();
        return nbValue.intValue();
    }
    
    protected int getMonth() throws UnexpectedTokenException, 
                                    DateParseException {
        doCheckType(TokenType.STRING);

        String value = (String) token.getValue();
        next();
        return MONTHS_MAP.get(value);
    }

    public List<Date> getParsed() {
        return Collections.unmodifiableList(parsed);
    }

    public Date getNow() {
        return new Date(now.getMillis());
    }

    public void setNow(Date now) {
        this.now = new DateTime().withMillis(now.getTime());
    }

}
