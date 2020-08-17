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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.security.KeyPair;
import java.security.PrivateKey;

import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.util.SecureString;

public class CryptedString extends SecureString implements Externalizable {

    private static final long serialVersionUID = 1L;
    private static final int AES_SIZE = 16;
    private byte[] rand;
    private String pubKeyMd5;

    public CryptedString() {}

    public CryptedString(KeyPair key, String secret) {
        try {
            if (secret == null) {
                data = null;
                length = 0;
            } else {
                length = secret.length();
                byte[] r = MBouncy.createRandom(AES_SIZE);
                rand = MBouncy.encryptRsa117(r, key.getPublic());
                data = secret.getBytes(MString.CHARSET_UTF_8);
                data = MBouncy.encryptAes(r, data);
            }
            this.pubKeyMd5 = MCrypt.md5(MBouncy.getPublicKey(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CryptedString(String pubKey, String secret) {
        try {
            if (secret == null) {
                data = null;
                length = 0;
            } else {
                length = secret.length();
                byte[] r = MBouncy.createRandom(AES_SIZE);
                rand = MBouncy.encryptRsa117(r, MBouncy.getPublicKey(pubKey));
                data = secret.getBytes(MString.CHARSET_UTF_8);
                data = MBouncy.encryptAes(r, data);
            }
            this.pubKeyMd5 = MCrypt.md5(pubKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String value(KeyPair key) {
        if (data == null) return null;
        try {
            byte[] r = MBouncy.decryptRsa117(rand, key.getPrivate());
            byte[] d = MBouncy.decryptAes(r, data);
            return new String(d, MString.CHARSET_UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String value(String privKey) {
        if (data == null) return null;
        try {
            PrivateKey key = MBouncy.getPrivateKey(privKey);

            byte[] r = MBouncy.decryptRsa117(rand, key);
            byte[] d = MBouncy.decryptAes(r, data);
            return new String(d, MString.CHARSET_UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getPublicKeyMd5() {
        return pubKeyMd5;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(length);
        if (data == null) out.writeInt(-1);
        else {
            out.writeInt(data.length);
            out.write(data);
            out.writeInt(rand.length);
            out.write(rand);
            out.writeObject(pubKeyMd5);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        length = in.readInt();
        int len = in.readInt();
        if (len < 0) {
            data = null;
            rand = null;
        } else {
            data = new byte[len];
            MFile.readBinary(in, data, 0, len);
            int l = in.readInt();
            rand = new byte[l];
            MFile.readBinary(in, rand, 0, l);
            pubKeyMd5 = (String) in.readObject();
        }
    }

    @Override
    public String value() {
        return (rand == null ? "?" : MBouncy.encodeBase64(rand))
                + "!"
                + (data == null ? "?" : MBouncy.encodeBase64(data));
    }

    public static SecureString create(String pubKey, String secret) {
        if (MString.isSet(pubKey)) return new CryptedString(pubKey, secret);
        return new SecureString(secret);
    }

    public static String value(SecureString string, KeyPair key) {
        if (string == null) return null;
        if (!(string instanceof CryptedString)) return string.value();
        return ((CryptedString) string).value(key);
    }
}
