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
import java.io.InputStream;

import de.mhus.lib.core.MMath;

public class CipherInputStream extends InputStream {

    private InputStream is;
    private CipherBlock cipher;

    public CipherInputStream(InputStream is) {
        this.is = is;
    }

    public CipherInputStream(InputStream is, CipherBlock cipher) {
        this.is = is;
        this.cipher = cipher;
    }

    @Override
    public int read() throws IOException {
        int out = is.read();
        if (out < 0 || cipher == null) return out;
        return MMath.unsignetByteToInt(cipher.decode((byte) out));
    }

    public CipherBlock getCipher() {
        return cipher;
    }

    public void setCipher(CipherBlock cipher) {
        this.cipher = cipher;
    }
}
