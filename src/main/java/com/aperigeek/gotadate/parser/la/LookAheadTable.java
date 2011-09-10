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

import com.aperigeek.gotadate.token.Token;
import com.aperigeek.gotadate.token.Tokenizer;
import com.aperigeek.gotadate.token.TokenizerException;
import java.util.LinkedList;

/**
 *
 * @author Vivien Barousse
 */
public class LookAheadTable {
    
    private Tokenizer source;
    
    private LinkedList<Token<? extends Object>> aheadQueue;

    public LookAheadTable(Tokenizer source) {
        this.source = source;
        aheadQueue = new LinkedList<Token<? extends Object>>();
    }
    
    public Token<? extends Object> get(int i) throws TokenizerException {
        ensureSize(i + 1);
        
        return aheadQueue.get(i);
    }
    
    public Token<? extends Object> pop() throws TokenizerException {
        ensureSize(1);
        
        return aheadQueue.pop();
    }
    
    protected void ensureSize(int size) throws TokenizerException {
        while (size >= aheadQueue.size()) {
            aheadQueue.add(source.next());
        }
    }
    
}
