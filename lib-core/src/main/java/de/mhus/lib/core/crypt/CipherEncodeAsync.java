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
package de.mhus.lib.core.crypt;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.LinkedList;

public class CipherEncodeAsync extends OutputStream {

    private AsyncKey key;
    private byte[] buffer;
    private int bufferPos;
    private MRandom random;
    private int bufferMin;
    private int bufferMax;
    private LinkedList<BigInteger> list = new LinkedList<>();

    public CipherEncodeAsync(AsyncKey key, MRandom random) {
        this.key = key;
        this.random = random;
        int cnt = Math.min(key.getMaxLoad() - 1, 254);
        buffer = new byte[cnt];
        bufferPos = 0;
        bufferMin = buffer.length / 3;
        bufferMax = buffer.length * 2 / 3;
    }

    @Override
    public void write(int b) throws IOException {
        bufferPos++;
        buffer[bufferPos] = (byte) b;

        if (bufferPos < bufferMin) return;

        if (bufferPos > bufferMax || random.getDouble() > 0.7) {
            flush();
        }
    }

    @Override
    public void flush() throws IOException {

        if (bufferPos == 0) return;

        buffer[0] = (byte) bufferPos;
        bufferPos++;

        // add random
        int rndCnt = Math.min(Math.abs(random.getInt()) + 1, buffer.length - bufferPos);
        for (int i = 0; i < rndCnt; i++) buffer[bufferPos + i] = random.getByte();

        byte[] bigEndian = new byte[bufferPos + rndCnt];
        for (int i = 0; i < bigEndian.length; i++) bigEndian[i] = buffer[bigEndian.length - i - 1];

        BigInteger org = new BigInteger(1, bigEndian);
        BigInteger enc = MCrypt.encode(key, org);
        list.add(enc);

        bufferPos = 0;
    }

    @Override
    public void close() throws IOException {
        flush();
    }

    public BigInteger[] toBigInteger() {
        return list.toArray(new BigInteger[list.size()]);
    }

    public void clear() {
        list.clear();
        bufferPos = 0;
    }
}
