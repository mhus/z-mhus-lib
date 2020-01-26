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

public class CipherOutputStream extends OutputStream {

    private CipherBlock cipher;
    private OutputStream os;

    public CipherOutputStream(OutputStream os, CipherBlock cipher) {
        this.os = os;
        this.cipher = cipher;
    }

    public CipherOutputStream(OutputStream os) {
        this.os = os;
    }

    @Override
    public void write(int b) throws IOException {
        if (cipher == null) os.write(b);
        else os.write(cipher.encode((byte) b));
    }

    public CipherBlock getCipher() {
        return cipher;
    }

    public void setCipher(CipherBlock cipher) {
        this.cipher = cipher;
    }
}
