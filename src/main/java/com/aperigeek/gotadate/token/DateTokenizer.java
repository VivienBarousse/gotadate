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

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

/**
 * Splits the given Reader into Token that can be recognized by the parser.
 * 
 * @author Vivien Barousse
 */
public class DateTokenizer implements Tokenizer {

    private Reader reader;

    private int line = 1;

    private int col = 0;

    private int ch;

    public DateTokenizer(Reader reader) throws IOException {
        this.reader = reader;
        
        try {
            readChar();
        } catch (EOFException ex) {
            // Will be catched at next call to readChar()
        }
    }

    protected void readChar() throws EOFException, IOException {
        int next = reader.read();
        if (next == -1) {
            ch = -1;
            throw new EOFException();
        }

        col++;
        if (next == '\r' || next == '\n') {
            col = 0;
            line++;
        }

        ch = (char) next;
    }

    public Token<? extends Object> next() throws IOException {
        try {
            while (Character.isSpaceChar(ch)) {
                readChar();
            }
        } catch (EOFException ex) {
            // Continue, will fail later
        }
        
        if (ch == -1) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        int tokenCol = this.col,
                tokenLine = this.line;
        
        TokenType type = TokenType.EMPTY;

        try {
            if (Character.isDigit(ch)) {
                type = TokenType.NUMBER;
                while (Character.isDigit(ch)) {
                    builder.append((char) ch);
                    readChar();
                }
            } else if (Character.isLetter(ch)) {
                type = TokenType.STRING;
                while (Character.isLetter(ch)) {
                    builder.append((char) ch);
                    readChar();
                }
            } else {
                type = TokenType.SEPARATOR;
                builder.append((char) ch);
                readChar();
            }
        } catch (EOFException ex) {
            // Continue, will fail at next call
        }
        
        Object value = builder.toString();
        if (type == TokenType.NUMBER) {
            Integer intValue = Integer.valueOf(value.toString());
            return new Token<Number>(type, intValue, tokenLine, tokenCol);
        } else if (type == TokenType.SEPARATOR) {
            Character chValue = value.toString().charAt(0);
            return new Token<Character>(type, chValue, tokenLine, tokenCol);
        }
        return new Token<Object>(type, value, tokenLine, tokenCol);
    }

}
