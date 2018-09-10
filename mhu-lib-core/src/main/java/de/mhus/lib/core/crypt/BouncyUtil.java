/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.crypt;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import de.mhus.lib.core.MApi;

public class BouncyUtil {

	public static void init() {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null)
			Security.addProvider(new BouncyCastleProvider());
	}

	protected static final String ALGORITHM = "RSA";
	protected static final String PROVIDER = "BC";
	protected static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
	
	// With help from https://github.com/ingoclaro/crypt-examples/tree/master/java/src
	public static KeyPair generateKey() throws NoSuchAlgorithmException, NoSuchProviderException
    {
		init();
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        keyGen.initialize(1024);
        KeyPair key = keyGen.generateKeyPair();
        return key;
    }
	
	// http://www.javased.com/index.php?api=java.security.PrivateKey
	public static PublicKey getPublicKey(String key) {
		try {
			init();
			byte[] encodedKey = decodeBASE64(key);
			X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
			return keyFactory.generatePublic(encodedKeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static PrivateKey getPrivateKey(String key) {
		try {
			init();
			byte[] encodedKey = decodeBASE64(key);
			PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
			return keyFactory.generatePrivate(encodedKeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getPublicKey(KeyPair key) {
		return encodeBASE64(new X509EncodedKeySpec(key.getPublic().getEncoded()).getEncoded());
	}

	public static String getPrivateKey(KeyPair key) {
		return encodeBASE64(new PKCS8EncodedKeySpec(key.getPrivate().getEncoded()).getEncoded());
	}

	public static byte[] encrypt(byte[] text, PublicKey key) throws Exception
    {
        byte[] cipherText = null;
        //
        // get an RSA cipher object and print the provider
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // encrypt the plaintext using the public key
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(text);
        return cipherText;
    }

    public static String encrypt(String text, PublicKey key) throws Exception
    {
        String encryptedText;
        byte[] cipherText = encrypt(text.getBytes("UTF8"),key);
        encryptedText = encodeBASE64(cipherText);
        return encryptedText;
    }

    public static byte[] decrypt(byte[] text, PrivateKey key) throws Exception
    {
        byte[] dectyptedText = null;
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text);
        return dectyptedText;

    }

    public static String decrypt(String text, PrivateKey key) throws Exception
    {
        String result;
        byte[] dectyptedText = decrypt(decodeBASE64(text),key);
        result = new String(dectyptedText, "UTF8");
        return result;

    }
    
    public static String encodeBASE64(byte[] bytes)
    {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] decodeBASE64(String text)
    {
        return Base64.getDecoder().decode(text);
    }

	public static byte[] createRand(int size) {
		byte[] out = new byte[size];
		MRandom rnd = MApi.lookup(MRandom.class);
		for (int i = 0; i < out.length; i++)
			out[i] = rnd.getByte();
		return out;
	}

}
