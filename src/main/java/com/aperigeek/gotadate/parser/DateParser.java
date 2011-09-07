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

import com.aperigeek.gotadate.token.DateTokenizer;
import com.aperigeek.gotadate.token.Token;
import com.aperigeek.gotadate.token.TokenType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Vivien Barousse
 */
public class DateParser {

    private DateTokenizer tokenizer;

    private Token token;

    private Token next;

    private List<Date> parsed = new ArrayList<Date>();

    public DateParser(DateTokenizer tokenizer) throws DateParseException {
        this.tokenizer = tokenizer;
        next();
    }

    protected void next() throws DateParseException {
        try {
            token = tokenizer.next();
        } catch (IOException ex) {
            throw new DateParseException("Unable to read from source", ex);
        }
    }

    public void parse() throws DateParseException {
        if (token == null) {
            return;
        }
        
        try {
            if (token.getType() == TokenType.NUMBER) {
                parseDate();
            }
        } catch (UnexpectedTokenException ex) {
        }
        
        next();
        parse();
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
    protected void parseDate() throws DateParseException, UnexpectedTokenException {
        int[] ls = new int[3];

        ls[0] = getInt();
        check('/');
        ls[1] = getInt();
        check('/');
        ls[2] = getInt();

        if (ls[1] > 12 && ls[0] <= 12) {
            int tmp = ls[1];
            ls[1] = ls[0];
            ls[0] = tmp;
        }

        Date date = new Date(ls[2] - 1900, ls[1] - 1, ls[0]);
        parsed.add(date);
    }

    protected void check(char ch) throws UnexpectedTokenException, DateParseException {
        doCheckType(TokenType.SEPARATOR);

        Character chVal = (Character) token.getValue();
        if (ch != chVal) {
            throw new UnexpectedTokenException();
        }
        next();
    }

    protected void checkType(TokenType type) throws DateParseException, UnexpectedTokenException {
        doCheckType(type);
        next();
    }

    protected void doCheckType(TokenType type) throws UnexpectedTokenException {
        if (token.getType() != type) {
            throw new UnexpectedTokenException();
        }
    }

    protected int getInt() throws DateParseException, UnexpectedTokenException {
        doCheckType(TokenType.NUMBER);

        Number nbValue = (Number) token.getValue();
        next();
        return nbValue.intValue();
    }

    public List<Date> getParsed() {
        return Collections.unmodifiableList(parsed);
    }

}
