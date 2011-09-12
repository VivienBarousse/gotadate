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
import org.joda.time.LocalTime;

/**
 *
 * @author Vivien Barousse
 */
public class DateParserTest extends TestCase {
    
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
    
    public void testObviousMMDDYYYY() throws TokenizerException, DateParseException {
        String date = "10/23/1988";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(88, 9, 23), parsed.get(0));
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
    
    public void testSimpleTimePMNoSeconds() throws TokenizerException, DateParseException {
        String date = "11:10 PM";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new LocalTime(23, 10).toDateTimeToday().toDate(), parsed.get(0));
    }
    
    public void testDateTime() throws TokenizerException, DateParseException {
        String date = "23/10/1988 23:10:55";
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 23, 23, 10, 55, DateTimeZone.getDefault()).toDate(), parsed.get(0));
    }
    
    public void testTimeDate() throws TokenizerException, DateParseException {
        String date = "23:10:55 23/10/1988";
        
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new DateTime(1988, 10, 23, 23, 10, 55, DateTimeZone.getDefault()).toDate(), parsed.get(0));
    }
    
}
