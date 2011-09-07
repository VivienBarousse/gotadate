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

import com.aperigeek.gotadate.TestCase;
import java.io.IOException;
import java.io.StringReader;

/**
 *
 * @author Vivien Barousse
 */
public class DateTokenizerTest extends TestCase {

    public DateTokenizerTest() {
    }
    
    public void testBasicBehaviour() throws TokenizerException {
        StringReader reader = new StringReader("abc def 123");
        DateTokenizer tokenizer = new DateTokenizer(reader);
        
        assertToken("abc", tokenizer.next());
        assertToken("def", tokenizer.next());
        assertToken(123, tokenizer.next());
        assertEquals(null, tokenizer.next());
    }
    
    public void testSpacesAtEOF() throws TokenizerException {
        StringReader reader = new StringReader("abc def 123     ");
        DateTokenizer tokenizer = new DateTokenizer(reader);
        
        assertToken("abc", tokenizer.next());
        assertToken("def", tokenizer.next());
        assertToken(123, tokenizer.next());
        assertEquals(null, tokenizer.next());
    }
    
    public void testSpacesBeforeFirst() throws TokenizerException {
        StringReader reader = new StringReader("     abc def 123");
        DateTokenizer tokenizer = new DateTokenizer(reader);
        
        assertToken("abc", tokenizer.next());
        assertToken("def", tokenizer.next());
        assertToken(123, tokenizer.next());
        assertEquals(null, tokenizer.next());
    }
    
    public void testSpecialChars() throws TokenizerException {
        StringReader reader = new StringReader("...!@#");
        DateTokenizer tokenizer = new DateTokenizer(reader);
        
        assertToken('.', tokenizer.next());
        assertToken('.', tokenizer.next());
        assertToken('.', tokenizer.next());
        assertToken('!', tokenizer.next());
        assertToken('@', tokenizer.next());
        assertToken('#', tokenizer.next());
        assertEquals(null, tokenizer.next());
    }
    
}
