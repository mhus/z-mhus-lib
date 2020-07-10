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
package de.mhus.lib.test;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import de.mhus.lib.core.io.StreamBuffer;
import de.mhus.lib.core.util.ByteBuffer;
import static org.junit.jupiter.api.Assertions.*;

public class MUtilsTest extends InternatTestCase {

    @Test
    public void testByteBuffer() {
        ByteBuffer buffer = new ByteBuffer();
        for (int i = 0; i < 1000; i++) { // 1024 * 10 buffer size, 1000 * 10
            buffer.append("Hello".getBytes());
            buffer.append("World".getBytes());
            assertEquals((i + 1) * 10, buffer.size());
        }
        byte[] res = buffer.toByte();
        for (int i = 0; i < 1000; i++) {
            assertEquals('H', res[i * 10 + 0]);
            assertEquals('e', res[i * 10 + 1]);
            assertEquals('l', res[i * 10 + 2]);
            assertEquals('l', res[i * 10 + 3]);
            assertEquals('o', res[i * 10 + 4]);
            assertEquals('W', res[i * 10 + 5]);
            assertEquals('o', res[i * 10 + 6]);
            assertEquals('r', res[i * 10 + 7]);
            assertEquals('l', res[i * 10 + 8]);
            assertEquals('d', res[i * 10 + 9]);
        }
    }

    @Test
    public void testByteStream() throws IOException {
        StreamBuffer stream = new StreamBuffer();
        for (int i = 0; i < 10000; i++) stream.getOutputStream().write((byte) (i % 100));
        assertEquals(10000, stream.size());

        for (int i = 0; i < 10000; i++) {
            byte x = (byte) (i % 100);
            int y = stream.getInputStream().read();
            assertEquals(x, y);
        }
        assertEquals(0, stream.size());

        // again
        for (int i = 0; i < 10000; i++) stream.getOutputStream().write((byte) (i % 100));
        assertEquals(10000, stream.size());

        for (int i = 0; i < 10000; i++) {
            byte x = (byte) (i % 100);
            int y = stream.getInputStream().read();
            assertEquals(x, y);
        }
        assertEquals(0, stream.size());
    }
}
