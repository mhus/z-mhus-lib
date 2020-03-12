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

import de.mhus.lib.errors.MException;

public class PemKey extends PemBlockModel implements PemPriv, PemPub {

    private boolean secret;

    public PemKey(PemKey clone, boolean secret) {
        super(clone.getName(), clone.getBlock());
        for (java.util.Map.Entry<String, Object> entry : clone) {
            put(entry.getKey(), entry.getValue());
        }
        this.secret = secret;
    }

    public PemKey(String name, byte[] block, boolean secret) {
        super(name, block);
        this.secret = secret;
    }

    public PemKey(String name, String block, boolean secret) {
        super(name, block);
        this.secret = secret;
    }

    public PemKey() {
        super();
    }

    public PemKey(PemBlock clone) {
        super(clone);
    }

    public PemKey(String name) {
        super(name);
    }

    @Override
    public String getMethod() throws MException {
        return getString(PemBlock.METHOD);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-----BEGIN ").append(getName()).append("-----\n");
        for (java.util.Map.Entry<String, Object> item : entrySet())
            sb.append(item.getKey()).append(": ").append(item.getValue()).append('\n');
        sb.append('\n');

        if (secret) {
            //			sb.append( Block.encodeSecret(getEncodedBlock()) );
            sb.append("?");
        } else sb.append(getEncodedBlock());
        sb.append("\n\n");
        sb.append("-----END ").append(getName()).append("-----\n");
        return sb.toString();
    }
}
