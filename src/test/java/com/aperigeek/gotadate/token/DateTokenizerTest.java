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
package com.aperigeek.gotadate.token;

import java.io.IOException;
import java.io.StringReader;
import junit.framework.TestCase;

/**
 *
 * @author Vivien Barousse
 */
public class DateTokenizerTest extends TestCase {

    public DateTokenizerTest() {
    }
    
    public void testBasicBehaviour() throws IOException {
        StringReader reader = new StringReader("abc def 123");
        DateTokenizer tokenizer = new DateTokenizer(reader);
        
        assertEquals("abc", tokenizer.next().getValue());
        assertEquals("def", tokenizer.next().getValue());
        assertEquals(123, tokenizer.next().getValue());
        assertEquals(null, tokenizer.next());
    }
    
    public void testSpacesAtEOF() throws IOException {
        StringReader reader = new StringReader("abc def 123     ");
        DateTokenizer tokenizer = new DateTokenizer(reader);
        
        assertEquals("abc", tokenizer.next().getValue());
        assertEquals("def", tokenizer.next().getValue());
        assertEquals(123, tokenizer.next().getValue());
        assertEquals(null, tokenizer.next());
    }
    
    public void testSpacesBeforeFirst() throws IOException {
        StringReader reader = new StringReader("     abc def 123");
        DateTokenizer tokenizer = new DateTokenizer(reader);
        
        assertEquals("abc", tokenizer.next().getValue());
        assertEquals("def", tokenizer.next().getValue());
        assertEquals(123, tokenizer.next().getValue());
        assertEquals(null, tokenizer.next());
    }
    
    public void testSpecialChars() throws IOException {
        StringReader reader = new StringReader("...!@#");
        DateTokenizer tokenizer = new DateTokenizer(reader);
        
        assertEquals('.', tokenizer.next().getValue());
        assertEquals('.', tokenizer.next().getValue());
        assertEquals('.', tokenizer.next().getValue());
        assertEquals('!', tokenizer.next().getValue());
        assertEquals('@', tokenizer.next().getValue());
        assertEquals('#', tokenizer.next().getValue());
        assertEquals(null, tokenizer.next());
    }
    
}
