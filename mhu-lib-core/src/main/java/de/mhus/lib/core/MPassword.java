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
package de.mhus.lib.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import de.mhus.lib.core.crypt.AsyncKey;
import de.mhus.lib.core.crypt.MCrypt;
import de.mhus.lib.core.crypt.MRandom;
import de.mhus.lib.core.crypt.Rot13;
import de.mhus.lib.core.io.TextReader;
import de.mhus.lib.core.util.SecureString;
import de.mhus.lib.core.vault.MVault;
import de.mhus.lib.core.vault.MVaultUtil;
import de.mhus.lib.core.vault.VaultEntry;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.errors.UsageException;

/**
 * Decode / Encode passwords. Attention: This do not give security in any way.
 * It's only a way to deny reading the password from the screen. No algorithm will
 * give you security in this case. Only one way algorithm is more secure.
 * 
 * The password is not allowed to start with a ":". Only the encoded password will
 * start with a ":" followed by the algorithm version and the encoded password.
 * 
 * A - old
 * X - not accepted - dummy password
 * B: - rot13
 * C: - Key Id from MVault
 * ZMD5: - md5 encoded with salt
 * @author mhu
 *
 */

public class MPassword {
		
	public enum METHOD {DUMMY,ROT13,RSA,HASH_MD5}

	public static final String PREFIX_DUMMY = "`X";
	public static final String PREFIX_ROT13 = "`B:";
	public static final String PREFIX_RSA = "`C:";
	public static final String PREFIX_HASH_MD5 = "`ZMD5:";
	public static final String PREFIX_SPECIAL1 = "`A";
	public static final String PREFIX = "`";
    private static MRandom random;

//	private static Log log = Log.getLog(MPassword.class);
		
	/**
	 * Encode a password string be aware of special characters like umlaute. This
	 * can cause problems.
	 * 
	 * @param in
	 * @return encoded string
	 */
	public static String encode(String in) {
		return encode(METHOD.ROT13,in, (String)null);
	}

	public static String encode(METHOD method, SecureString in, String secret) {
		return encode(method, in.value(), secret);
	}

	public static String encode(METHOD method, SecureString in, SecureString secret) {
		return encode(method, in.value(), secret.value());
	}
	
	public static String encode(METHOD method, String in, SecureString secret) {
		return encode(method, in, secret.value());
	}
	
	public static String encode(METHOD method, String in, String secret) {
		if (in == null) return null;
		if (isEncoded(in)) return in;
		switch (method) {
			case DUMMY: // empty dummy password
				return PREFIX_DUMMY;
			case ROT13:
				return PREFIX_ROT13 + Rot13.encode(in);
			case RSA:
				MVault vault = MVaultUtil.loadDefault();
				VaultEntry entry = vault.getEntry(UUID.fromString(secret));
				if (entry == null) throw new MRuntimeException("key not found",secret);
				try {
					AsyncKey key = MVaultUtil.adaptTo(entry, AsyncKey.class);
					return PREFIX_RSA + entry.getId() + ":" + MCrypt.encodeWithSalt(key, in);
				} catch (Exception e) {
					throw new MRuntimeException(e);
				}
			case HASH_MD5:
				return PREFIX_HASH_MD5 + encodePasswordMD5(secret);
			default:
				throw new MRuntimeException("unknown encode method",method);
		}
	}

	public static String encode(METHOD method, String in) {
		return encode(method, in, (String)null);
	}

	public static boolean isEncoded(String in) {
		if (in == null) return false;
		return in.startsWith("`");
	}

	/**
	 * Decode a encoded password.
	 * 
	 * @param in
	 * @return decoded string
	 */
	public static String decode(String in) {
		if (in == null) return null;
		if (!isEncoded(in)) return in;
		if (in.startsWith(PREFIX_ROT13))
			return Rot13.decode(in.substring(3));
		if (in.startsWith(PREFIX_RSA)) {
			in = in.substring(3);
			int p = in.indexOf(':');
			if (p < 0) throw new UsageException("key id not found");
			String keyId = in.substring(0, p);
			in = in.substring(p+1);
			MVault vault = MVaultUtil.loadDefault();
			VaultEntry entry = vault.getEntry(UUID.fromString(keyId));
			if (entry == null) throw new MRuntimeException("key not found",keyId);
			try {
				AsyncKey key = MVaultUtil.adaptTo(entry, AsyncKey.class);
				return MCrypt.decodeWithSalt(key, in);
			} catch (Exception e) {
				throw new MRuntimeException(e);
			}
		}
		if (in.startsWith(PREFIX_DUMMY))
			throw new MRuntimeException("try to encode a dummy password");
		if (in.startsWith(PREFIX_SPECIAL1)) {
			StringBuilder out = new StringBuilder();
			for (int i = 2; i < in.length(); i++) {
				char c = in.charAt(i);
				switch (c) {
				case '0': c = '9'; break;
				case '1': c = '0'; break;
				case '2': c = '1'; break;
				case '3': c = '2'; break;
				case '4': c = '3'; break;
				case '5': c = '4'; break;
				case '6': c = '5'; break;
				case '7': c = '6'; break;
				case '8': c = '7'; break;
				case '9': c = '8'; break;
				}
				out.append(c);
			}
			return out.toString();
		}
		return in;
	}
	
	public static boolean validatePasswordMD5(String real, String md5) {
		if (md5 == null || real == null || md5.length() < 2) return false;
		if (md5.startsWith(PREFIX_HASH_MD5))
			md5 = md5.substring(MPassword.PREFIX_HASH_MD5.length());
		return MCrypt.validateMd5WithSalt(md5, real);
	}
	
	public static String encodePasswordMD5(String real) {
		if (!real.startsWith(MPassword.PREFIX_HASH_MD5))
			real = MPassword.PREFIX_HASH_MD5 + MCrypt.md5WithSalt(real);
		return real;
	}

	public static String forceEncodePasswordMD5(String real) {
		real = MPassword.PREFIX_HASH_MD5 + MCrypt.md5WithSalt(real);
		return real;
	}

	public static String sha1(String ... input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        for (String in : input)
        	mDigest.update(in.getBytes());
        byte[] result = mDigest.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
    }	
	
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.print("decoded: ");
			TextReader reader = new TextReader(System.in);
			args = new String[] { reader.readLine() };
		}
		System.out.println("encoded: " + encode(args[0]));
	}
	    
	public static String generate(int min, int max, boolean upper, boolean numbers, boolean specials) {
		char[] symbols = new char[ 75 - 3 ];
		int i = 0;
		for (char c = 'a'; c <= 'z'; c++)
			if ( c != 'l')
				symbols[i++] = c;
		if (upper)
			for (char c = 'A'; c <= 'Z'; c++)
				if (c != 'I' && c != 'O')
					symbols[i++] = c;
		if (numbers)
			for (char c = '0'; c <= '9'; c++)
				symbols[i++] = c;
		if (specials) {
				symbols[i++] = '_';
				symbols[i++] = '-';
				symbols[i++] = '.';
				symbols[i++] = '!';
				symbols[i++] = '+';
				symbols[i++] = '/';
				symbols[i++] = '@';
				symbols[i++] = '#';
				symbols[i++] = ';';
		}
		return generate(max == min ? min : (getRandomInt(max-min)+max), symbols, i);
	}
	
	public static String generate(int length, char[] symbols, int symbolLength) {
		char[] buf = new char[length];
		for (int idx = 0; idx < buf.length; ++idx) 
		      buf[idx] = symbols[getRandomInt(symbolLength)];
		    return new String(buf);
	}
	
	public static String generate(int length, String symbols) {
		char[] buf = new char[length];
		for (int idx = 0; idx < buf.length; ++idx) 
		      buf[idx] = symbols.charAt(getRandomInt(symbols.length()));
		    return new String(buf);
	}

	private synchronized static MRandom getRandom() {
	    if (random == null)
	        random = M.l(MRandom.class);
        return random;
    }

    private static int getRandomInt(int max) {
        return getRandom().getInt() % max;
    }
    
    /**
	 * Check if the passwords are equals. The password could also be hashes.
	 * 
	 * @param storedPass
	 * @param givenPass
	 * @return true if found the are identically
	 */
	public static boolean equals(String storedPass, String givenPass) {
		if (storedPass == null || givenPass == null) return false;
		// do not accept empty pass
		storedPass = storedPass.trim();
		givenPass = givenPass.trim();
		if (givenPass.length() == 0) return false;
		if (givenPass.startsWith("`")) {
			//givenPass = decode(givenPass);
			return false; // given password can't be encoded, this will give the ability to use the encoded string itself as password instead of clear text
		}
		if (storedPass.startsWith(PREFIX)) {
			if (storedPass.startsWith(PREFIX_HASH_MD5)) {
				return validatePasswordMD5(givenPass, storedPass);
			}
			storedPass = decode(storedPass);
		}
		return storedPass.equals(givenPass);
	}

}
