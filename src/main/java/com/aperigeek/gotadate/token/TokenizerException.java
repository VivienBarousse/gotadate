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

/**
 *
 * @author Vivien Barousse
 */
public class TokenizerException extends Exception {

    public TokenizerException() {
    }

    public TokenizerException(String msg) {
        super(msg);
    }

    public TokenizerException(Throwable cause) {
        super(cause);
    }

    public TokenizerException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
