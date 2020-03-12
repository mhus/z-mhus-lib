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
package de.mhus.lib.core.crypt.pem;

import java.security.PrivateKey;

import de.mhus.lib.core.util.SecureString;

public class SecurityPrivateKey implements PrivateKey {

    private static final long serialVersionUID = 1L;
    private String algorithm;
    private String format;
    private byte[] encoded;

    public SecurityPrivateKey(PemBlock pem, SecureString passphrase) throws Exception {
        if (!PemUtil.isPrivKey(pem))
            throw new SecurityException("Block is not a private key: " + pem.getName());
        algorithm = pem.getString(PemBlock.METHOD, "");
        format = pem.getString(PemBlock.FORMAT, "");
        encoded = pem.getBytesBlock();
        if (passphrase != null) {
            encoded = PemUtil.decrypt(pem, passphrase);
        }
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public byte[] getEncoded() {
        return encoded;
    }
}
