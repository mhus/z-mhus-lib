/*
 *  Copyright (C) 2002-2004 Mike Hummel
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package de.mhus.lib.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import de.mhus.lib.core.io.TextReader;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.Rot13;

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

	private static Log log = Log.getLog(MPassword.class);
	
//	private static Log log = Log.getLog(MPassword.class);
	
	/**
	 * Encode a password string be aware of special characters like umlaute. This
	 * can cause problems.
	 * 
	 * @param in
	 * @return
	 */
	public static String encode(String in) {
		return encode(1,in);
	}
	
	public static String encode(int method, String in) {
		if (in == null) return null;
		if (isEncoded(in)) return in;
		switch (method) {
			case 1:
				return ":1" + Rot13.encode(in);
			case 2:
				// use a local key store ... TODO do it next
		}
		return null;
	}

	public static boolean isEncoded(String in) {
		if (in == null) return false;
		return in.startsWith(":");
	}

	/**
	 * Decode a encoded password.
	 * 
	 * @param in
	 * @return
	 */
	public static String decode(String in) {
		if (in == null) return null;
		if (!isEncoded(in)) return in;
		if (in.startsWith(":1"))
			return Rot13.decode(in.substring(2));
//		if (in.startsWith(":2"))
//			return new String( Base64.decodeBase64( MCast.fromBinaryString(in.substring(2))));
		
		return in;
	}
	
	public static String encodePasswordMD5(String real) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(real.getBytes());
			return MCast.toBinaryString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			log.t(e);
		}
		return null;
	}

	public static String sha1(String ... input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        for (String in : input)
        	mDigest.update(in.getBytes());
        byte[] result = mDigest.digest();
        StringBuffer sb = new StringBuffer();
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

	public static boolean validatePassword(String current, String saved) {
		// TODO check encoding or null values
		return encodePasswordMD5(current).equals(saved);
	}

}
