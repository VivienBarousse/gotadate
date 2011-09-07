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
package com.aperigeek.gotadate;

import com.aperigeek.gotadate.token.Token;
import com.aperigeek.gotadate.token.TokenType;
import junit.framework.AssertionFailedError;

/**
 *
 * @author Vivien Barousse
 */
public abstract class TestCase extends junit.framework.TestCase {
    
    public static void assertToken(String value, Token actual) {
        assertToken(TokenType.STRING, value, actual);
    }
    
    public static void assertToken(Number value, Token actual) {
        assertToken(TokenType.NUMBER, value, actual);
    }
    
    public static void assertToken(Character value, Token actual) {
        assertToken(TokenType.SEPARATOR, value, actual);
    }
    
    public static void assertToken(TokenType type, Object value, Token actual) {
        if (actual.getType() != type ||
                !actual.getValue().equals(value)) {
            throw new AssertionFailedError();
        }
    }
    
}
