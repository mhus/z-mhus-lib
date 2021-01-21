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

import de.mhus.lib.core.MMath;

/**
 * TODO Not Working !!! rotate left encode and right for decode current block value.
 *
 * @author mikehummel
 */
public class CipherBlockRotate implements CipherBlock {

    private byte[] block;
    private int pos;

    public CipherBlockRotate(int size) {
        block = new byte[size];
    }

    public byte[] getBlock() {
        return block;
    }

    public int getSize() {
        return block.length;
    }

    @Override
    public void reset() {
        pos = 0;
    }

    @Override
    public byte encode(byte in) {
        in = MMath.rotl(in, block[pos]);
        next();
        return in;
    }

    @Override
    public byte decode(byte in) {
        in = MMath.rotr(in, block[pos]);
        next();
        return in;
    }

    private void next() {
        pos = (pos + 1) % block.length;
    }
}
