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

import com.aperigeek.gotadate.TestCase;
import com.aperigeek.gotadate.token.DateTokenizer;
import com.aperigeek.gotadate.token.TokenizerException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

/**
 *
 * @author Vivien Barousse
 */
public class DateParserTest extends TestCase {
    
    public void testSimpleNumber() throws TokenizerException, DateParseException {
        String date = "23";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(0, parsed.size());
    }
    
    public void testSimpleDate() throws TokenizerException, DateParseException {
        String date = "23/10/1988";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testSimpleDateFollowedByInvalidDate() throws TokenizerException, DateParseException {
        String date = "23/10/1988 23:abc";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateWithTextBefore() throws TokenizerException, DateParseException {
        String date = "abc def 23/10/1988";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateWithTextAfter() throws TokenizerException, DateParseException {
        String date = "23/10/1988 abc def";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateWithTextBeforeAndAfter() throws TokenizerException, DateParseException {
        String date = "abc 23/10/1988 def";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateObviousMMDDYYYY() throws TokenizerException, DateParseException {
        String date = "10/23/1988";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateFullMonth() throws TokenizerException, DateParseException {
        String date = "23 October 1988";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateFullMonthNoYear() throws TokenizerException, DateParseException {
        String date = "23 October";
        Date then = new LocalDate(1988, 1, 1).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateFullMonthFirst() throws TokenizerException, DateParseException {
        String date = "October 23 1988";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateFullMonthFirstWithComma() throws TokenizerException, DateParseException {
        String date = "October 23, 1988";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateFullMonthFirstNoYear() throws TokenizerException, DateParseException {
        String date = "October 23rd";
        Date then = new LocalDate(1988, 1, 1).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateOrdinalFullMonthNoYear() throws TokenizerException, DateParseException {
        String date = "23rd October";
        Date then = new LocalDate(1988, 1, 1).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
    }
    
    public void testDateYesterday() throws TokenizerException, DateParseException {
        String date = "yesterday";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalDate(1988, 10, 22).toDate(), parsed.get(0));
    }
    
    public void testDateYesterdayCaseUnsensitive() throws TokenizerException, DateParseException {
        String date = "Yesterday";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalDate(1988, 10, 22).toDate(), parsed.get(0));
    }
    
    public void testDateYesterdayCaseUnsensitive2() throws TokenizerException, DateParseException {
        String date = "YESTERDAY";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalDate(1988, 10, 22).toDate(), parsed.get(0));
    }
    
    public void testDateTomorrow() throws TokenizerException, DateParseException {
        String date = "tomorrow";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalDate(1988, 10, 24).toDate(), parsed.get(0));
    }
    
    public void testSimpleTime() throws TokenizerException, DateParseException {
        String date = "23:10:55";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalTime(23, 10, 55).toDateTimeToday().toDate(), parsed.get(0));
    }
    
    public void testSimpleTimeFollowedByIncorrectDate() throws TokenizerException, DateParseException {
        String date = "23:10:55 23/abc";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalTime(23, 10, 55).toDateTimeToday().toDate(), parsed.get(0));
    }
    
    public void testSimpleTimeNoSeconds() throws TokenizerException, DateParseException {
        String date = "23:10";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalTime(23, 10).toDateTimeToday().toDate(), parsed.get(0));
    }
    
    public void testSimpleTimeWithThen() throws TokenizerException, DateParseException {
        String date = "23:10:55";
        LocalDate then = new LocalDate(1988, 10, 23);
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then.toDate());
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 23, 23, 10, 55, DateTimeZone.getDefault()).toDate(), parsed.get(0));
    }
    
    public void testSimpleTimePM() throws TokenizerException, DateParseException {
        String date = "11:10:55 PM";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalTime(23, 10, 55).toDateTimeToday().toDate(), parsed.get(0));
    }
    
    public void testSimpleTimePMCaseUnsensitive() throws TokenizerException, DateParseException {
        String date = "11:10:55 pm";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalTime(23, 10, 55).toDateTimeToday().toDate(), parsed.get(0));
    }
    
    public void testSimpleTimePMNoSeconds() throws TokenizerException, DateParseException {
        String date = "11:10 PM";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalTime(23, 10).toDateTimeToday().toDate(), parsed.get(0));
    }
    
    public void testSimpleTimePMNoSecondsOrMinutes() throws TokenizerException, DateParseException {
        String date = "11 PM";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalTime(23, 0).toDateTimeToday().toDate(), parsed.get(0));
    }
    
    public void testTimeSingleNumberWithAt() throws TokenizerException, DateParseException {
        String date = "at 11";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalTime(11, 0).toDateTimeToday().toDate(), parsed.get(0));
    }
    
    public void testDateTime() throws TokenizerException, DateParseException {
        String date = "23/10/1988 23:10:55";
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 23, 23, 10, 55).toDate(), parsed.get(0));
    }
    
    public void testTimeDate() throws TokenizerException, DateParseException {
        String date = "23:10:55 23/10/1988";
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 23, 23, 10, 55).toDate(), parsed.get(0));
    }
    
    public void testDateTimeAt() throws TokenizerException, DateParseException {
        String date = "tomorrow at 10";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 24, 10, 0).toDate(), parsed.get(0));
    }
    
    public void testDateTimeAtPM() throws TokenizerException, DateParseException {
        String date = "tomorrow at 6 PM";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 24, 18, 0).toDate(), parsed.get(0));
    }
    
    public void testTimeDesambiguation6() throws TokenizerException, DateParseException {
        String date = "tomorrow at 6";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 24, 18, 0).toDate(), parsed.get(0));
    }
    
    public void testTimeDesambiguation7() throws TokenizerException, DateParseException {
        String date = "tomorrow at 7";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 24, 19, 0).toDate(), parsed.get(0));
    }
    
    public void testTimeDesambiguation8() throws TokenizerException, DateParseException {
        String date = "tomorrow at 8";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 24, 8, 0).toDate(), parsed.get(0));
    }
    
    public void testTimeDesambiguation9() throws TokenizerException, DateParseException {
        String date = "tomorrow at 9";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 24, 9, 0).toDate(), parsed.get(0));
    }
    
    public void testDateRelativeDaysAgo() throws TokenizerException, DateParseException {
        String date = "2 days ago";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 21, 0, 0).toDate(), parsed.get(0));
    }
    
    public void testDateRelativeDaysWithTime() throws TokenizerException, DateParseException {
        String date = "2 days ago at 9";
        Date then = new LocalDate(1988, 10, 23).toDate();
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.setNow(then);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 21, 9, 0).toDate(), parsed.get(0));
    }
    
}
