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

import junit.framework.TestCase;

/**
 *
 * @author Vivien Barousse
 */
public class TokenTest extends TestCase {
    
    public void testAccessors() {
        Token token = new Token("abc", 12, 42);
        assertEquals("abc", token.getValue());
        assertEquals(12, token.getLine());
        assertEquals(42, token.getCol());
    }
    
}
