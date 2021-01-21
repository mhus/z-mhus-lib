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
package de.mhus.lib.core.crypt;

import java.io.IOException;
import java.io.OutputStream;

import de.mhus.lib.core.MMath;

public class SaltOutputStream extends OutputStream {

    private OutputStream next;
    private MRandom random;
    private int cnt;
    private boolean addRandomBlocks;
    private byte salt;
    private int maxBlockSize;

    /**
     * @param next
     * @param random
     * @param maxBlockSize
     * @param addRandomBlocks
     */
    public SaltOutputStream(
            OutputStream next, MRandom random, int maxBlockSize, boolean addRandomBlocks) {
        this.next = next;
        this.random = random;
        this.addRandomBlocks = addRandomBlocks;
        this.maxBlockSize = maxBlockSize;
        cnt = 0;
    }

    @Override
    public void write(int b) throws IOException {
        cnt--;
        if (cnt <= 0) {

            if (addRandomBlocks) {
                cnt = MMath.unsignetByteToInt(random.getByte()) % maxBlockSize;
                next.write(cnt);
                for (int i = 0; i < cnt; i++) next.write(random.getByte());
            }

            salt = random.getByte();
            cnt = MMath.unsignetByteToInt(random.getByte()) % maxBlockSize;
            next.write(salt);
            next.write(cnt);
        }

        b = MMath.addRotate((byte) b, salt);
        next.write(b);
    }

    @Override
    public void close() throws IOException {
        next.close();
    }

    @Override
    public void flush() throws IOException {
        next.flush();
    }
}
