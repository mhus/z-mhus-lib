package de.mhus.lib.core.crypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

// https://stackoverflow.com/questions/23561104/how-to-encrypt-and-decrypt-string-with-my-passphrase-in-java-pc-not-mobile-plat
public class Blowfish {

	public static byte[] encrypt(byte[] strClearText,String strKey) throws Exception{
        SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes(),"Blowfish");
        Cipher cipher=Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
        byte[] encrypted=cipher.doFinal(strClearText);
        return encrypted;
	}
	
	public static byte[] decrypt(byte[] strEncrypted, String strKey) throws Exception{
        SecretKeySpec skeyspec=new SecretKeySpec(strKey.getBytes(),"Blowfish");
        Cipher cipher=Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, skeyspec);
        byte[] decrypted=cipher.doFinal(strEncrypted);
        return decrypted;
	}
	
}
