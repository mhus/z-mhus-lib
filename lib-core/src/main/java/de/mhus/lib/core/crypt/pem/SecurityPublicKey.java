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
package de.mhus.lib.core.crypt.pem;

import java.security.PublicKey;

public class SecurityPublicKey implements PublicKey {

    private static final long serialVersionUID = 1L;
    private String algorithm;
    private String format;
    private byte[] encoded;

    public SecurityPublicKey(PemBlock pem) {
        if (!PemUtil.isPubKey(pem))
            throw new SecurityException("Block is not a public key: " + pem.getName());
        algorithm = pem.getString(PemBlock.METHOD, "");
        format = pem.getString(PemBlock.FORMAT, "");
        encoded = pem.getBytesBlock();
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
