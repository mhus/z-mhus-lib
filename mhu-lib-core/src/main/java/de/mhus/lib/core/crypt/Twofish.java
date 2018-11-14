package de.mhus.lib.core.crypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.util.Base64;

public class Twofish {

	public static String encrypt(String strClearText,String strKey) throws Exception{
		return Base64.encode(encrypt(strClearText.getBytes("utf-8"), strKey));
	}
	
	public static String decrypt(String strEncrypted,String strKey) throws Exception{
		return new String(decrypt(Base64.decode(strEncrypted), strKey), "utf-8");
	}

	public static byte[] encrypt(byte[] strClearText,String strKey) throws Exception{
		strKey = fixKey(strKey);
        SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes("utf-8"),"twofish");
        Cipher cipher=Cipher.getInstance("twofish", MBouncy.PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
        byte[] encrypted=cipher.doFinal(strClearText);
        return encrypted;
	}
	
	public static byte[] decrypt(byte[] strEncrypted, String strKey) throws Exception{
		strKey = fixKey(strKey);
        SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes("utf-8"),"twofish");
        Cipher cipher=Cipher.getInstance("twofish", MBouncy.PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, skeyspec);
        byte[] decrypted=cipher.doFinal(strEncrypted);
        return decrypted;
	}

	private static String fixKey(String strKey) {
		// min key size 64bit - 8byte, fill with spaces
		if (strKey.length() < 8)
			strKey = strKey + MString.rep(' ', 8 - strKey.length());
		return strKey;
	}

}
