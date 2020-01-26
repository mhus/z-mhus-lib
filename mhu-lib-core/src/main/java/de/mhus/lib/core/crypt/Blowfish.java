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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.mhus.lib.core.util.Base64;

// https://stackoverflow.com/questions/23561104/how-to-encrypt-and-decrypt-string-with-my-passphrase-in-java-pc-not-mobile-plat
public class Blowfish {

    public static String encrypt(String strClearText, String strKey) throws Exception {
        return Base64.encode(encrypt(strClearText.getBytes("utf-8"), strKey));
    }

    public static String decrypt(String strEncrypted, String strKey) throws Exception {
        return new String(decrypt(Base64.decode(strEncrypted), strKey), "utf-8");
    }

    public static byte[] encrypt(byte[] strClearText, String strKey) throws Exception {
        MBouncy.init();
        strKey = fixKey(strKey);
        SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes("utf-8"), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish", MBouncy.PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
        byte[] encrypted = cipher.doFinal(strClearText);
        return encrypted;
    }

    public static byte[] decrypt(byte[] strEncrypted, String strKey) throws Exception {
        MBouncy.init();
        strKey = fixKey(strKey);
        SecretKeySpec skeyspec = new SecretKeySpec(strKey.getBytes("utf-8"), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish", MBouncy.PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, skeyspec);
        byte[] decrypted = cipher.doFinal(strEncrypted);
        return decrypted;
    }

    private static String fixKey(String strKey) {
        if (strKey.length() == 0) return " ";
        return strKey;
    }
}
