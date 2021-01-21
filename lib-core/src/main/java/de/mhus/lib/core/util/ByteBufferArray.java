/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.util;

public class ByteBufferArray {

    int extend = 1024 * 10;
    int next = 0;

    byte[] buffer = null;

    public ByteBufferArray() {
        buffer = new byte[extend];
    }

    public ByteBufferArray(int initial) {
        buffer = new byte[initial];
    }

    public ByteBufferArray(int initial, int extend) {
        buffer = new byte[initial];
        this.extend = extend;
    }

    public void append(byte in) {
        if (next >= buffer.length) {
            byte[] newBuffer = new byte[buffer.length + extend];
            System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
            buffer = newBuffer;
        }
        buffer[next] = in;
        next++;
    }

    public void append(byte[] in) {
        append(in, 0, in.length);
    }

    public void append(byte[] in, int offset, int len) {
        // if ( offset + len > in.length ) len = in.length - offset; // hmmm
        // ....
        if (next + len > buffer.length) {
            byte[] newBuffer = new byte[buffer.length + Math.max(extend, len)];
            System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
            buffer = newBuffer;
        }
        try {
            System.arraycopy(in, offset, buffer, next, len);
        } catch (IndexOutOfBoundsException ioe) {
            System.out.println(
                    "FATAL: ByteBufferArray.append: "
                            + in.length
                            + ' '
                            + offset
                            + ' '
                            + buffer.length
                            + ' '
                            + next
                            + ' '
                            + len);
            throw ioe;
        }
        next += len;
    }

    public int getSize() {
        return next;
    }

    public byte[] toByte() {
        byte[] newBuffer = new byte[next];
        System.arraycopy(buffer, 0, newBuffer, 0, next);
        return newBuffer;
    }

    public boolean isCurrentlyFull() {
        return next == buffer.length;
    }

    public byte[] getInternalBuffer() {
        return buffer;
    }

    public void clear() {
        buffer = new byte[0];
        next = 0;
    }
}
