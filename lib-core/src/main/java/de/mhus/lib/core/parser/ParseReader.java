/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.parser;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class ParseReader {

    private Reader str;
    private char current;
    private boolean closed = false;
    private int pos = -1;

    public ParseReader(Reader str) throws IOException {
        this.str = str;
        nextCurrent();
    }

    private void nextCurrent() throws IOException {
        if (closed) return;
        int read = str.read();
        pos++;
        if (read == -1) {
            closed = true;
        }
        current = (char) read;
    }

    public char character() throws EOFException {
        if (closed) throw new EOFException();
        return current;
    }

    public void consume() throws IOException {
        nextCurrent();
    }

    public boolean isClosed() {
        return closed;
    }

    public int getPosition() {
        return pos;
    }
}
