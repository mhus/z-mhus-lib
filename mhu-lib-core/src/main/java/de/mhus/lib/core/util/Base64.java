/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * TODO need to rewrite the class
 * 
 */

package de.mhus.lib.core.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;

/**
 * <p>Base64 class.</p>
 *
 * @author Brian Wing Shun Chan
 * @version $Id: $Id
 * @since 3.2.9
 */
public class Base64 {

	private static final Log log = Log.getLog(Base64.class);
	
	/**
	 * <p>decode.</p>
	 *
	 * @param base64 a {@link java.lang.String} object.
	 * @return an array of byte.
	 */
	public static byte[] decode(String base64) {
		if (MString.isEmpty(base64)) {
			return new byte[0];
		}

		int pad = 0;

		for (int i = base64.length() - 1; base64.charAt(i) == '=';
				i--) {

			pad++;
		}

		int length = (base64.length() * 6) / 8 - pad;
		byte[] raw = new byte[length];
		int rawindex = 0;

		for (int i = 0; i < base64.length(); i += 4) {
			int block = getValue(base64.charAt(i)) << 18;

			block += getValue(base64.charAt(i + 1)) << 12;
			block += getValue(base64.charAt(i + 2)) << 6;
			block += getValue(base64.charAt(i + 3));

			for (int j = 0; j < 3 && rawindex + j < raw.length; j++) {
				raw[rawindex + j] = (byte)(block >> 8 * (2 - j) & 0xff);
			}

			rawindex += 3;
		}

		return raw;
	}

	/**
	 * <p>encode.</p>
	 *
	 * @param raw an array of byte.
	 * @return a {@link java.lang.String} object.
	 */
	public static String encode(byte[] raw) {
		return encode(raw, 0, raw.length);
	}

	/**
	 * <p>encode.</p>
	 *
	 * @param raw an array of byte.
	 * @param offset a int.
	 * @param length a int.
	 * @return a {@link java.lang.String} object.
	 */
	public static String encode(byte[] raw, int offset, int length) {
		int lastIndex = Math.min(raw.length, offset + length);

		StringBuilder sb = new StringBuilder(
			((lastIndex - offset) / 3 + 1) * 4);

		for (int i = offset; i < lastIndex; i += 3) {
			sb.append(encodeBlock(raw, i, lastIndex));
		}

		return sb.toString();
	}

	/**
	 * <p>objectToString.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String objectToString(Object o) {
		if (o == null) {
			return null;
		}

		ByteArrayOutputStream ubaos = new ByteArrayOutputStream(
			32000);

		try {
			ObjectOutputStream os = new ObjectOutputStream(ubaos);

			os.flush();
			os.writeObject(o);
			os.flush();
		}
		catch (Exception e) {
			log.e(e, e);
		}

		return encode(ubaos.toByteArray(), 0, ubaos.size());
	}

	/**
	 * <p>encodeBlock.</p>
	 *
	 * @param raw an array of byte.
	 * @param offset a int.
	 * @param lastIndex a int.
	 * @return an array of char.
	 */
	protected static char[] encodeBlock(byte[] raw, int offset, int lastIndex) {
		int block = 0;
		int slack = lastIndex - offset - 1;
		int end = slack < 2 ? slack : 2;

		for (int i = 0; i <= end; i++) {
			byte b = raw[offset + i];

			int neuter = b >= 0 ? ((int) (b)) : b + 256;
			block += neuter << 8 * (2 - i);
		}

		char[] base64 = new char[4];

		for (int i = 0; i < 4; i++) {
			int sixbit = block >>> 6 * (3 - i) & 0x3f;
			base64[i] = getChar(sixbit);
		}

		if (slack < 1) {
			base64[2] = '=';
		}

		if (slack < 2) {
			base64[3] = '=';
		}

		return base64;
	}

	/**
	 * <p>getChar.</p>
	 *
	 * @param sixbit a int.
	 * @return a char.
	 */
	protected static char getChar(int sixbit) {
		if ((sixbit >= 0) && (sixbit <= 25)) {
			return (char)(65 + sixbit);
		}

		if ((sixbit >= 26) && (sixbit <= 51)) {
			return (char)(97 + (sixbit - 26));
		}

		if ((sixbit >= 52) && (sixbit <= 61)) {
			return (char)(48 + (sixbit - 52));
		}

		if (sixbit == 62) {
			return '+';
		}

		return sixbit != 63 ? '?' : '/';
	}

	/**
	 * <p>getValue.</p>
	 *
	 * @param c a char.
	 * @return a int.
	 */
	protected static int getValue(char c) {
		if ((c >= 'A') && (c <= 'Z')) {
			return c - 65;
		}

		if ((c >= 'a') && (c <= 'z')) {
			return (c - 97) + 26;
		}

		if ((c >= '0') && (c <= '9')) {
			return (c - 48) + 52;
		}

		if (c == '+') {
			return 62;
		}

		if (c == '/') {
			return 63;
		}

		return c != '=' ? -1 : 0;
	}

	/**
	 * <p>encode.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String encode(String value) {
		if (value == null) return "";
		return encode(value.getBytes());
	}

}
