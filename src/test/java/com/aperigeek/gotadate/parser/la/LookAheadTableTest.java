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
package com.aperigeek.gotadate.parser.la;

import com.aperigeek.gotadate.TestCase;
import com.aperigeek.gotadate.token.DateTokenizer;
import com.aperigeek.gotadate.token.Tokenizer;
import com.aperigeek.gotadate.token.TokenizerException;
import java.io.Reader;
import java.io.StringReader;

/**
 *
 * @author Vivien Barousse
 */
public class LookAheadTableTest extends TestCase {

    public void testGet() throws TokenizerException {
        Reader reader = new StringReader("0 1 2 3 4 5 6 7");
        Tokenizer tokenizer = new DateTokenizer(reader);
        LookAheadTable table = new LookAheadTable(tokenizer);
        assertToken(0, table.get(0));
        assertToken(1, table.get(1));
        assertToken(2, table.get(2));
        assertToken(3, table.get(3));
        assertToken(4, table.get(4));
        assertToken(5, table.get(5));
        assertToken(6, table.get(6));
        assertToken(7, table.get(7));
        assertNull(table.get(8));
    }

    public void testPop() throws Exception {
        Reader reader = new StringReader("0 1 2 3 4 5 6 7");
        Tokenizer tokenizer = new DateTokenizer(reader);
        LookAheadTable table = new LookAheadTable(tokenizer);
        assertToken(0, table.pop());
        assertToken(1, table.pop());
        assertToken(2, table.pop());
        assertToken(3, table.pop());
        assertToken(4, table.pop());
        assertToken(5, table.pop());
        assertToken(6, table.pop());
        assertToken(7, table.pop());
        assertNull(table.pop());
    }
    
}
