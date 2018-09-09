package de.mhus.lib.test;

import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.crypt.CryptedString;
import de.mhus.lib.core.crypt.MCrypt;
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
			String a = MCrypt.getPublicKey(key);
			PublicKey b = MCrypt.getPublicKey(a);
			assertEquals(key.getPublic(), b);
		}
		{
			String a = MCrypt.getPrivateKey(key);
			PrivateKey b = MCrypt.getPrivateKey(a);
			assertEquals(key.getPrivate(), b);
		}
	}
	
	public void testCryptedStringTextual() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String text = Lorem.create();
		KeyPair key = CryptedString.generateKey();
		String publKey = MCrypt.getPublicKey(key);
		String privKey = MCrypt.getPrivateKey(key);

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

}
