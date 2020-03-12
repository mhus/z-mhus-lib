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
import java.math.BigInteger;
import java.util.Base64;
import java.util.Enumeration;

import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;

/**
 * Need to separate this method to remove the dependency to bounycastle from the MCrypt class. Do
 * not use it directly.
 *
 * @author mikehummel
 */
class Asn1Util {

    // http://luca.ntop.org/Teaching/Appunti/asn1.html
    /**
     * Load a RSA private key into a AsyncKey object.
     *
     * @param key key as ASN1 encoded string containing "-----BEGIN RSA PRIVATE KEY-----"
     * @return Corresponding key object
     * @throws IOException If the key start or stop token was not found
     */
    static AsyncKey loadPrivateRsaKey(String key) throws IOException {

        int pos = key.indexOf("-----BEGIN RSA PRIVATE KEY-----\n");
        if (pos < 0) throw new IOException("begin of RSA Key not found");

        key = key.substring(pos + "-----BEGIN RSA PRIVATE KEY-----\n".length());

        pos = key.indexOf("-----END RSA PRIVATE KEY-----");
        if (pos < 0) throw new IOException("end of RSA Key not found");

        key = key.substring(0, pos);
        key = key.replace("\n", "").trim();
        byte[] asn = Base64.getDecoder().decode(key);

        ASN1Sequence primitive = (ASN1Sequence) ASN1Sequence.fromByteArray(asn);
        Enumeration<?> ex = primitive.getObjects();
        BigInteger v = ((ASN1Integer) ex.nextElement()).getValue();

        int version = v.intValue();
        if (version != 0 && version != 1) {
            throw new IOException("wrong version for RSA private key");
        }
        BigInteger modulus = ((ASN1Integer) ex.nextElement()).getValue();
        BigInteger publicExponent = ((ASN1Integer) ex.nextElement()).getValue();
        BigInteger privateExponent = ((ASN1Integer) ex.nextElement()).getValue();
        BigInteger prime1 = ((ASN1Integer) ex.nextElement()).getValue();
        BigInteger prime2 = ((ASN1Integer) ex.nextElement()).getValue();
        BigInteger exponent1 = ((ASN1Integer) ex.nextElement()).getValue();
        BigInteger exponent2 = ((ASN1Integer) ex.nextElement()).getValue();
        BigInteger coefficient = ((ASN1Integer) ex.nextElement()).getValue();

        return new AsyncKey(
                modulus,
                publicExponent,
                privateExponent,
                prime1,
                prime2,
                exponent1,
                exponent2,
                coefficient,
                MCrypt.getMaxLoad(modulus));
    }

    /**
     * Load a RSA public key into a AsyncKey object.
     *
     * @param key key as ASN1 encoded string containing "-----BEGIN RSA PUBLIC KEY-----"
     * @return Corresponding key object
     * @throws IOException If the key start or stop token was not found
     */
    static AsyncKey loadPublicRsaKey(String key) throws IOException {

        int pos = key.indexOf("-----BEGIN RSA PUBLIC KEY-----\n");
        if (pos < 0) throw new IOException("begin of RSA Key not found");

        key = key.substring(pos + "-----BEGIN RSA PUBLIC KEY-----\n".length());

        pos = key.indexOf("-----END RSA PUBLIC KEY-----");
        if (pos < 0) throw new IOException("end of RSA Key not found");

        key = key.substring(0, pos);
        key = key.replace("\n", "").trim();
        byte[] asn = Base64.getDecoder().decode(key);

        ASN1Sequence primitive = (ASN1Sequence) ASN1Sequence.fromByteArray(asn);
        Enumeration<?> ex = primitive.getObjects();
        BigInteger v = ((ASN1Integer) ex.nextElement()).getValue();

        int version = v.intValue();
        if (version != 0 && version != 1) {
            throw new IOException("wrong version for RSA private key");
        }
        BigInteger modulus = ((ASN1Integer) ex.nextElement()).getValue();
        BigInteger publicExponent = ((ASN1Integer) ex.nextElement()).getValue();

        return new AsyncKey(
                modulus,
                publicExponent,
                null,
                null,
                null,
                null,
                null,
                null,
                MCrypt.getMaxLoad(modulus));
    }
}
