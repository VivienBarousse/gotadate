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
        assertEquals(new Date(0, 0, 1, 23, 10, 55), parsed.get(0));
    }
    
    public void testSimpleTimeNoSeconds() throws TokenizerException, DateParseException {
        String date = "23:10";
        DateTokenizer tokenizer = new DateTokenizer(new StringReader(date));
        DateParser parser = new DateParser(tokenizer);
        parser.parse();
        List<Date> parsed = parser.getParsed();
        
        assertEquals(1, parsed.size());
        assertEquals(new Date(0, 0, 1, 23, 10, 0), parsed.get(0));
    }
    
    
}
