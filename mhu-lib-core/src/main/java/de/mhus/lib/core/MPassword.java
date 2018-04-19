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
import java.util.Random;
import java.util.UUID;

import de.mhus.lib.core.crypt.AsyncKey;
import de.mhus.lib.core.crypt.MCrypt;
import de.mhus.lib.core.crypt.MRandom;
import de.mhus.lib.core.crypt.Rot13;
import de.mhus.lib.core.io.TextReader;
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
 * @author mhu
 *
 */

public class MPassword {
	
	public static final int TYPE_DUMMY = 0;
	public static final int TYPE_ROT13 = 1;
	public static final int TYPE_RSA   = 2;

//	private static Log log = Log.getLog(MPassword.class);
		
	/**
	 * Encode a password string be aware of special characters like umlaute. This
	 * can cause problems.
	 * 
	 * @param in
	 * @return encoded string
	 */
	public static String encode(String in) {
		return encode(TYPE_ROT13,in, null);
	}

	public static String encode(int method, String in, String secret) {
		if (in == null) return null;
		if (isEncoded(in)) return in;
		switch (method) {
			case 0: // empty dummy password
				return "`X";
			case 1:
				return "`B:" + Rot13.encode(in);
			case 2:
				MVault vault = MVaultUtil.loadDefault();
				VaultEntry entry = vault.getEntry(UUID.fromString(secret));
				if (entry == null) throw new MRuntimeException("key not found",secret);
				try {
					AsyncKey key = entry.adaptTo(AsyncKey.class);
					return "`C:" + entry.getId() + ":" + MCrypt.encodeWithSalt(key, in);
				} catch (Exception e) {
					throw new MRuntimeException(e);
				}

		}
		return null;
	}

	public static String encode(int method, String in) {
		return encode(method, in, null);
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
		if (in.startsWith("`B:"))
			return Rot13.decode(in.substring(3));
		if (in.startsWith("`C:")) {
			in = in.substring(3);
			int p = in.indexOf(':');
			if (p < 0) throw new UsageException("key id not found");
			String keyId = in.substring(0, p);
			in = in.substring(p+1);
			MVault vault = MVaultUtil.loadDefault();
			VaultEntry entry = vault.getEntry(UUID.fromString(keyId));
			if (entry == null) throw new MRuntimeException("key not found",keyId);
			try {
				AsyncKey key = entry.adaptTo(AsyncKey.class);
				return MCrypt.decodeWithSalt(key, in);
			} catch (Exception e) {
				throw new MRuntimeException(e);
			}
		}
		if (in.startsWith("`X"))
			throw new MRuntimeException("try to encode a dummy password");
		if (in.startsWith("`A")) {
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
		return MCrypt.validateMd5WithSalt(md5, real);
	}
	
	public static String encodePasswordMD5(String real) {
		return MCrypt.md5WithSalt(real);
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
	
    private static final Random random = new Random();
    
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
		return generate(max == min ? min : (random.nextInt(max-min)+max), symbols, i);
	}
	
	public static String generate(int length, char[] symbols, int symbolLength) {
		char[] buf = new char[length];
		for (int idx = 0; idx < buf.length; ++idx) 
		      buf[idx] = symbols[random.nextInt(symbolLength)];
		    return new String(buf);
	}
	
	public static String generate(int length, String symbols) {
		char[] buf = new char[length];
		for (int idx = 0; idx < buf.length; ++idx) 
		      buf[idx] = symbols.charAt(random.nextInt(symbols.length()));
		    return new String(buf);
	}

}
