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
package de.mhus.lib.core.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.errors.MRuntimeException;

/**
 * @author Brian Wing Shun Chan
 */
public class Base64 {

	private static final Log log = Log.getLog(Base64.class);
	
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

	public static String encode(byte[] raw) {
		return encode(raw, 0, raw.length);
	}

	public static String encode(byte[] raw, int offset, int length) {
		int lastIndex = Math.min(raw.length, offset + length);

		StringBuilder sb = new StringBuilder(
			((lastIndex - offset) / 3 + 1) * 4);

		for (int i = offset; i < lastIndex; i += 3) {
			sb.append(encodeBlock(raw, i, lastIndex));
		}

		return sb.toString();
	}

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

	public static String encode(String value) {
		if (value == null) return "";
		return encode(value.getBytes());
	}

	public static String decodeToString(String encoded) {
		try {
			return new String(decode(encoded),"utf-8") ;
		} catch (UnsupportedEncodingException e) {
			throw new MRuntimeException(encoded,e);
		}
	}

}