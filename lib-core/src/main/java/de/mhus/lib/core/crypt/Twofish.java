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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.util.Base64;

public class Twofish {

    public static String encrypt(String strClearText, String strKey) throws Exception {
        return Base64.encode(encrypt(strClearText.getBytes("utf-8"), strKey));
    }

    public static String decrypt(String strEncrypted, String strKey) throws Exception {
        return new String(decrypt(Base64.decode(strEncrypted), strKey), "utf-8");
    }

    private static String normalizeKey(String strKey) {
        if (strKey == null) return "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
        if (strKey.length() < 16)
            strKey = strKey + MString.rep('\0', 16 - strKey.length());
        else
        if (strKey.length() < 24)
            strKey = strKey + MString.rep('\0', 24 - strKey.length());
        else
        if (strKey.length() < 32)
            strKey = strKey + MString.rep('\0', 32 - strKey.length());
        else
        if (strKey.length() > 32)
            strKey = strKey.substring(0, 32);
        return strKey;
    }

    public static byte[] encrypt(byte[] strClearText, String strKey) throws Exception {
        strKey = normalizeKey(strKey);
        MBouncy.init();
        SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes("utf-8"), "twofish");
        Cipher cipher = Cipher.getInstance("twofish", MBouncy.PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
        byte[] encrypted = cipher.doFinal(strClearText);
        return encrypted;
    }

    public static byte[] decrypt(byte[] strEncrypted, String strKey) throws Exception {
        strKey = normalizeKey(strKey);
        MBouncy.init();
        SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes("utf-8"), "twofish");
        Cipher cipher = Cipher.getInstance("twofish", MBouncy.PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, skeyspec);
        byte[] decrypted = cipher.doFinal(strEncrypted);
        return decrypted;
    }

}
