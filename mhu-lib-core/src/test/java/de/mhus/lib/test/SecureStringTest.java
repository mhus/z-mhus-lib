package de.mhus.lib.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.crypt.BouncyUtil;
import de.mhus.lib.core.crypt.CryptedString;
import de.mhus.lib.core.util.Lorem;
import de.mhus.lib.core.util.SecureString;
import junit.framework.TestCase;

public class SecureStringTest extends TestCase {

	public void testSecureString() {
		String text = Lorem.create();
		SecureString sec = new SecureString(text);
		String text2 = sec.value();
		assertEquals(text, text2);
	}

	public void testSecureStringSerialization() throws ClassNotFoundException, IOException {
		String text = Lorem.create();
		SecureString sec = new SecureString(text);
		
		String serial = MCast.serializeToString(sec);
		SecureString sec2 = (SecureString) MCast.unserializeFromString(serial, getClass().getClassLoader());

		String text2 = sec2.value();
		assertEquals(text, text2);
	}

	public void testCryptedString() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String text = Lorem.create();
		KeyPair key = CryptedString.generateKey();
		CryptedString sec = new CryptedString(key, text);
		String text2 = sec.value(key);
		// positive check
		assertEquals(text, text2);
		
		// negative check
		Field field = sec.getClass().getDeclaredField("data");
		field.setAccessible(true);
		byte[] data = (byte[])field.get(sec);
		assertFalse(text.equals(new String(data,MString.CHARSET_CHARSET_UTF_8)));
	}
	
	public void testKeyConvert() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		KeyPair key = CryptedString.generateKey();
		{
			String a = BouncyUtil.getPublicKey(key);
			PublicKey b = BouncyUtil.getPublicKey(a);
			assertEquals(key.getPublic(), b);
		}
		{
			String a = BouncyUtil.getPrivateKey(key);
			PrivateKey b = BouncyUtil.getPrivateKey(a);
			assertEquals(key.getPrivate(), b);
		}
	}
	
	public void testCryptedStringTextual() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String text = Lorem.create();
		KeyPair key = CryptedString.generateKey();
		String publKey = BouncyUtil.getPublicKey(key);
		String privKey = BouncyUtil.getPrivateKey(key);

		CryptedString sec = new CryptedString(publKey, text);
		String text2 = sec.value(privKey);
		// positive check
		assertEquals(text, text2);
		
		// negative check
		Field field = sec.getClass().getDeclaredField("data");
		field.setAccessible(true);
		byte[] data = (byte[])field.get(sec);
		assertFalse(text.equals(new String(data,MString.CHARSET_CHARSET_UTF_8)));
	}
	
	public void testCryptedStringPerformance() {
		String text = Lorem.create();
		int cnt = 0;
		long start = System.currentTimeMillis();
		KeyPair key = CryptedString.generateKey();
		while (!MTimeInterval.isTimeOut(start, 1000)) {
			cnt++;
			CryptedString sec = new CryptedString(key, text);
			sec.value(key);
		}
		
		System.out.println("CryptedStringPerformance " + cnt + "/s");
	}

	/**
	 * This is a example how to use CryptedKey in your code. Use the key pool
	 * to rotate keys, send the public key to the secret encoded and decode the secret.
	 */
	public void testKeyPool() {
		
		String text = Lorem.create();

		KeyPair key = BouncyUtil.getRsaKeyFromPool();
		String pub = BouncyUtil.getPublicKey(key);
		CryptedString sec = callToGetTheSecret(pub, text);
		
		String text2 = sec.value(key);
		assertEquals(text, text2);
	}

	/**
	 * The trusted other side, but not a trusted transfer way
	 * @param pub
	 * @param text
	 * @return
	 */
	private CryptedString callToGetTheSecret(String pub, String text) {
		return new CryptedString(pub, text);
	}
	
	public void testCryptedStringSerialization() throws IOException, ClassNotFoundException {
		
		String text = Lorem.create();

		KeyPair key = BouncyUtil.getRsaKeyFromPool();
		String pub = BouncyUtil.getPublicKey(key);
		CryptedString sec = callToGetTheSecret(pub, text);

		String serial = MCast.serializeToString(sec);
		CryptedString sec2 = (CryptedString) MCast.unserializeFromString(serial, getClass().getClassLoader());
		
		String text2 = sec2.value(key);
		assertEquals(text, text2);

	}
	
}
