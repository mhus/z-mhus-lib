/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.core.crypt;

import java.io.IOException;
import java.io.InputStream;

import de.mhus.lib.core.MMath;

public class SaltInputStream extends InputStream {

    private InputStream previous;
    private boolean hasRandomBlocks;
    private int cnt;
    private byte salt;

    public SaltInputStream(InputStream previous, boolean hasRandomBlocks) {
        this.previous = previous;
        this.hasRandomBlocks = hasRandomBlocks;
        cnt = 0;
    }

    @Override
    public int read() throws IOException {
        cnt--;
        if (cnt <= 0) {
            if (hasRandomBlocks) {
                int c = previous.read();
                if (c < 0) return c;
                for (int i = 0; i < c; i++) {
                    int r = previous.read();
                    if (r < 0) return r;
                }
            }

            int s = previous.read();
            if (s < 0) return s;
            salt = (byte) s;
            cnt = previous.read();
            if (cnt < 0) return cnt;
        }

        int out = previous.read();
        if (out < 0) return out;

        out = MMath.unsignetByteToInt(MMath.subRotate((byte) out, salt));

        return out;
    }

    @Override
    public void close() throws IOException {
        previous.close();
    }
}
