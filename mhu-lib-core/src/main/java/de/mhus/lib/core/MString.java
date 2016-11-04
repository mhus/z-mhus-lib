/*
 * ./core/de/mhu/lib/AString.java
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

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author hummel
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MString {

	public static final String CHARSET_UTF_8 = "UTF-8";
	public static final String CHARSET_UTF_16 = "UTF-16";
	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";

	public static final char[] WHITESPACE = new char[] { ' ', '\n', '\r', '\t' };
	public static final String DEFAULT_SEPARATOR = ",";
	private static final String[] DEFAULT_SELECTION_SEPARATOR = new String[] {"\n\n"};
	public static final String CHARSET_DEFAULT = CHARSET_UTF_8;

	/**
	 * Return true if the the string is not empty also trimmed.
	 * 
	 * @param _in The string to test
	 * @return true if the string is not null and not an empty string and not only contains whitespaces
	 */
	public static boolean isSetTrim(String _in) {
		return !isEmptyTrim(_in);
	}
	
	/**
	 * Return true if the the string is not empty.
	 * 
	 * @param _in The string to test
	 * @return true if the string is not null and not an empty string
	 */
	public static boolean isSet(String _in) {
		return !isEmpty(_in);
	}
	
	/**
	 * Returns true if the string is null or an empty string.
	 * 
	 * @param _in
	 * @return
	 */
	public static boolean isEmpty(String _in) {
		return (_in == null || _in.length() == 0);
	}

	/**
	 * Split the string in parts. Separator is a pattern. In difference of the
	 * build in split it will not remove empty parts and the pattern is not a regex.
	 * 
	 * @param _in
	 * @param _pattern
	 * @return
	 */
	public static String[] split(String _in, String _pattern) {

		if (_in == null)
			return new String[0];

		int nr = 0;
		int offset = 0;
		while ((offset = _in.indexOf(_pattern, offset)) != -1) {
			nr++;
			offset += _pattern.length();
		}

		String[] out = new String[nr + 1];

		nr = 0;
		offset = 0;
		int oldOffset = 0;
		while ((offset = _in.indexOf(_pattern, offset)) != -1) {
			out[nr] = _in.substring(oldOffset, offset);
			nr++;
			offset += _pattern.length();
			oldOffset = offset;
		}
		out[nr] = _in.substring(oldOffset);

		return out;

	}

	/**
	 * Split the string in parts. Separator is a pattern. In difference of the
	 * build in the pattern is not a regex.
	 * 
	 * @param _in
	 * @param _pattern
	 * @return
	 */
	public static String[] splitIgnoreEmpty(String _in, String _pattern) {
		return splitIgnoreEmpty(_in, _pattern, false);
	}
	
	public static String[] splitIgnoreEmpty(String _in, String _pattern,boolean trim) {

		if (_in == null)
			return new String[0];

		int offset = 0;
		int oldOffset = 0;
		Vector<String> out = new Vector<String>();
		while ((offset = _in.indexOf(_pattern, offset)) != -1) {
			String s = _in.substring(oldOffset, offset);
			if (trim) s = s.trim();
			if (s.length() != 0)
				out.add(s);
			offset += _pattern.length();
			oldOffset = offset;
		}
		String s = _in.substring(oldOffset);
		if (trim) s = s.trim();
		if (s.length() != 0)
			out.add(s);
		return out.toArray(new String[out.size()]);

	}

	/**
	 * Replace all pattern thru replacement strings. Pattern is not a regex.
	 * 
	 * @param _src
	 * @param _pattern
	 * @param _replacement
	 * @return
	 */
	public static String replaceAll(String _src, String _pattern,
			String _replacement) {
		StringBuffer sb = new StringBuffer(_src);
		replaceAll(sb, _pattern, _replacement);
		return sb.toString();
	}

	/**
	 * Replace all pattern thru replacement strings. Pattern is not a regex.
	 * 
	 * @param _src
	 * @param _pattern
	 * @param _replacement
	 */
	public static void replaceAll(StringBuffer _src, String _pattern,
			String _replacement) {

		int offset = 0;
		int len = _pattern.length();
		int diff = _replacement.length() - len + 1;

		while ((offset = _src.indexOf(_pattern, offset)) >= 0) {
			_src.replace(offset, offset + len, _replacement);
			offset += diff;
		}

	}

	/**
	 * Return, if the char is located in the string (indexOf).
	 * @param _s 
	 * @param _c 
	 * @return 
	 */

	public static boolean isIndex(String _s, String _c) {
		int p = _s.indexOf(_c);
		if (p < 0)
			return false;
		else
			return true;
	}

	/**
	 * Return the string before _c in _s. If _c is not found in _s, return is an
	 * empty string.
	 * @param _s 
	 * @param _c 
	 * @return 
	 */
	public static String beforeIndex(String _s, String _c) {
		int p = _s.indexOf(_c);
		if (p < 0)
			return "";

		return _s.substring(0, p);
	}

	/**
	 * Return the string after _c in _s. If _c is not found in _s, return is an
	 * empty string.
	 * @param _s 
	 * @param _c 
	 * @return 
	 */
	public static String afterIndex(String _s, String _c) {
		int p = _s.indexOf(_c);
		if (p < 0)
			return "";

		return _s.substring(p + _c.length());
	}

	/**
	 * Return, if the char is located in the string (indexOf).
	 * @param _s 
	 * @param _c 
	 * @return 
	 */

	public static boolean isIndex(String _s, char _c) {
		int p = _s.indexOf(_c);
		if (p < 0)
			return false;
		else
			return true;
	}

	/**
	 * Return the string before _c in _s. If _c is not found in _s, return is an
	 * empty string.
	 * @param _s 
	 * @param _c 
	 * @return 
	 */
	public static String beforeIndex(String _s, char _c) {
		int p = _s.indexOf(_c);
		if (p < 0)
			return "";

		return _s.substring(0, p);
	}

	/**
	 * Return the string after _c in _s. If _c is not found in _s, return is an
	 * empty string.
	 * @param _s 
	 * @param _c 
	 * @return 
	 */
	public static String afterIndex(String _s, char _c) {
		int p = _s.indexOf(_c);
		if (p < 0)
			return "";

		return _s.substring(p + 1);
	}
	
	/**
	 * Returns true if one of the chars is found in the string.
	 * 
	 * @param _s
	 * @param _c
	 * @return
	 */
	public static boolean isIndex(String _s, char[] _c) {

		for (int i = 0; i < _c.length; i++) {
			if (_s.indexOf(_c[i]) >= 0)
				return true;
		}
		return false;
	}

	/**
	 * Return the string before _c in _s. If _c is not found in _s, return is an
	 * empty string.
	 * @param _s 
	 * @param _c 
	 * @return 
	 */
	public static String beforeIndex(String _s, char[] _c) {

		int p = -1;
		for (int i = 0; i < _c.length; i++) {
			if ((p = _s.indexOf(_c[i])) >= 0)
				return _s.substring(0, p);
		}
		return "";
	}

	/**
	 * Return the string after _c in _s. If _c is not found in _s, return is an
	 * empty string.
	 * @param _s 
	 * @param _c 
	 * @return 
	 */
	public static String afterIndex(String _s, char[] _c) {

		int p = -1;
		for (int i = 0; i < _c.length; i++) {
			if ((p = _s.indexOf(_c[i])) >= 0)
				return _s.substring(p + 1);
		}
		return "";
	}

	/**
	 * Returns the position where the first of one of the chars is found in the string.
	 * 
	 * @param _s
	 * @param _c
	 * @param offset
	 * @return
	 */
	public static int indexOf(String _s, char[] _c, int offset) {

		for (int i = offset; i < _s.length(); i++) {
			char c = _s.charAt(i);
			for (int j = 0; j < _c.length; j++)
				if (c == _c[j])
					return i;
		}
		return -1;
	}

	/**
	 * Returns the position where the first of one of the chars is found in the string.
	 *
	 * @param _s
	 * @param _c
	 * @return
	 */
	public static int indexOf(String _s, char[] _c) {
		return indexOf(_s, _c, 0);
	}

	/**
	 * Encodes for the web. Same like MXml.encode
	 * 
	 * @param _in
	 * @return
	 */
	public static String encodeWebUnicode(String _in) {

		StringBuffer out = new StringBuffer();
		for (int i = 0; i < _in.length(); i++) {
			char c = _in.charAt(i);
			if (c > 255) {
				out.append("&#" + (int) c + ";");
			} else if (c == '&') {
				out.append("&amp;");
			} else
				out.append(c);

		}
		return out.toString();
	}

	
	public static boolean isUnicode(String _in, char _joker) {
		for (int i = 0; i < _in.length(); i++) {
			char c = _in.charAt(i);
			if (c > 255 || c == _joker)
				return true;
		}
		return false;
	}

	public static String encodeUnicode(String _in) {

		if (_in == null)
			return "";

		if (!isUnicode(_in, '\\'))
			return _in;

		StringBuffer out = new StringBuffer();
		for (int i = 0; i < _in.length(); i++) {
			char c = _in.charAt(i);
			if (c > 255) {
				out.append("\\u" + MCast.toHex2String(c / 256)
						+ MCast.toHex2String(c % 256));
			} else if (c == '\\') {
				out.append("\\\\");
			} else
				out.append(c);

		}
		return out.toString();
	}

	public static String encodeURIComponent(String _in) {

		if (_in == null)
			return "";

		StringBuffer out = new StringBuffer();
		for (int i = 0; i < _in.length(); i++) {
			char c = _in.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'
					|| c == '-' || c == '.' || c == '!' || c == '~' || c == '*'
					|| c == '\'' || c == '(' || c == ')') {
				out.append(c);
			} else {
				out.append('%' + MCast.toHex2String(c));
			}

		}
		return out.toString();
	}

	public static String decodeUnicode(String _in) {

		int mode = 0;
		byte[] buffer = new byte[4];

		if (_in == null)
			return "";

		StringBuffer out = new StringBuffer();
		for (int i = 0; i < _in.length(); i++) {
			char c = _in.charAt(i);
			switch (mode) {
			case 0:
				if (c == '\\')
					mode = 1;
				else
					out.append(c);
				break;
			case 1:
				if (c == 'u')
					mode = 2;
				else if (c == 'n') {
					out.append('\n');
					mode = 0;
				} else if (c == 'r') {
					out.append('\r');
					mode = 0;
				} else if (c == 't') {
					out.append('\t');
					mode = 0;
				} else
					out.append('\\' + c);
				break;
			case 2:
				buffer[0] = (byte) c;
				mode = 3;
				break;
			case 3:
				buffer[1] = (byte) c;
				mode = 4;
				break;
			case 4:
				buffer[2] = (byte) c;
				mode = 5;
				break;
			case 5:
				buffer[3] = (byte) c;
				out.append((char) MCast.tointFromHex(new String(buffer)));
				mode = 0;
				break;

			}
		}
		return out.toString();
	}

	public static String decodeURIComponent(String _in) {

		int mode = 0;
		byte[] buffer = new byte[2];

		if (_in == null)
			return "";

		StringBuffer out = new StringBuffer();
		for (int i = 0; i < _in.length(); i++) {
			char c = _in.charAt(i);
			switch (mode) {
			case 0:
				if (c == '%')
					mode = 1;
				else
					out.append(c);
				break;
			case 1:
				buffer[0] = (byte) c;
				mode = 2;
				break;
			case 2:
				buffer[1] = (byte) c;
				out.append((char) MCast.tointFromHex(new String(buffer)));
				mode = 0;
				break;
			}
		}
		return out.toString();
	}

	public static String encodeCSVLine(String[] _fields) {

		if (_fields == null || _fields.length == 0)
			return "";

		StringBuffer out = new StringBuffer();
		for (int i = 0; i < _fields.length; i++) {
			if (i != 0)
				out.append(',');
			out.append(encodeCSV(_fields[i]));
		}

		return out.toString();

	}

	public static String encodeCSV(String _field) {

		if (_field == null)
			return "\"\"";
		return '"' + _field.replaceAll("\\\"", "\"\"") + '"';

	}

	/*
	 * public static String[] decodeCSVLine( String _fields ) {
	 * 
	 * StringBuffer field = new StringBuffer(); Vector out = new Vector();
	 * 
	 * csvParseSep( _fields, 0 );
	 * 
	 * }
	 */

	public static String decodeCSV(String _field) {

		if (_field == null || _field.length() == 0)
			return "";

		_field = _field.trim();
		if (_field.startsWith("\"") && _field.endsWith("\"")) {
			_field = _field.substring(1, _field.length() - 1);
		}

		if (_field.length() == 0)
			return "";

		return _field.replaceAll("\\\"\\\"", "\"");

	}

	public static boolean compareRegexPattern(String str, String pattern) {
		if (str == null || pattern == null) return false;
		return str.matches(pattern);
	}
	
	/**
	 * Use % at the beginning and/or the end
	 * example: %aloa% or !%aloa%
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static boolean compareSQLLikePattern(String str, String pattern) {
		if (str == null || pattern == null) return false;
		boolean policy = true;
		if (pattern.startsWith("!")) {
			policy = false;
			pattern = pattern.substring(1);
		}
		if (pattern.endsWith("%") && pattern.startsWith("%")) {
			if (str.indexOf(pattern.substring(1, pattern.length() - 2)) >= 0)
				return policy;
		} else if (pattern.startsWith("%")) {
			if (str.endsWith(pattern.substring(1)))
				return policy;
		} else if (pattern.endsWith("%")) {
			if (str.startsWith(pattern.substring(0, pattern.length() - 1)))
				return policy;
		} else if (pattern.equals(str)) {
			return policy;
		}
		return !policy;
	}

	/**
	 * Use * at the beginning or/and the end and a ! at the beginning for 'not'.
	 * e.g. *aloa* !*aloa*
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static boolean compareFsLikePattern(String str, String pattern) {
		if (str == null || pattern == null) return false;
		boolean ret = true;
		if (pattern.startsWith("!")) {
			ret = false;
			pattern = pattern.substring(1);
		}

		if (pattern.equals("*"))
			return ret;

		if (pattern.endsWith("*") && pattern.startsWith("*")) {
			if (str.indexOf(pattern.substring(1, pattern.length() - 2)) >= 0)
				return ret;
		} else if (pattern.startsWith("*")) {
			if (str.endsWith(pattern.substring(1)))
				return ret;
		} else if (pattern.endsWith("*")) {
			if (str.startsWith(pattern.substring(0, pattern.length() - 1)))
				return ret;
		} else if (pattern.equals(str)) {
			return ret;
		}
		return !ret;
	}

	public static String replaceSQLLikePattern(String str, String pattern,
			String replace) {
		if (pattern.endsWith("%") && pattern.startsWith("%")) {
			int pos = str.indexOf(pattern.substring(1, pattern.length() - 2));
			if (pos >= 0) {
				return str.substring(0, pos) + replace
						+ str.substring(pos + pattern.length() - 2);
			}
		} else if (pattern.startsWith("%")) {
			if (str.endsWith(pattern.substring(1))) {
				return str.substring(0, str.length() - pattern.length() + 1)
						+ replace;
			}
		} else if (pattern.endsWith("%")) {
			if (str.startsWith(pattern.substring(0, pattern.length() - 1))) {
				return replace + str.substring(pattern.length() - 1);
			}
		} else if (pattern.equals(str)) {
			return replace;
		}
		return str;
	}

	public static String join(String[] src, char glue) {
		if (src == null)
			return null;
		if (src.length == 0)
			return "";
		if (src.length == 1)
			return src[0];
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < src.length; i++) {
			if (i != 0)
				sb.append(glue);
			sb.append(src[i]);
		}
		return sb.toString();
	}

	public static String join(List<?> src, String glue) {
		return join(src.iterator(), glue);
	}
	
	public static String join(Iterator<?> src, String glue) {
		if (src == null)
			return null;
		StringBuffer sb = new StringBuffer();
		int i = 0;
		while (src.hasNext()) {
			if (i != 0)
				sb.append(glue);
			i++;
			sb.append(src.next());
		}
		return sb.toString();
	}

	/**
	 * Convert String to canonical standard form. null -> "". Trims lead trail
	 * blanks.
	 * 
	 * @param s
	 *            String to be converted.
	 * 
	 * @return String in canonical form.
	 */
	public static String canonical(String s) {
		if (s == null) {
			return "";
		} else {
			return s.trim();
		}
	} // end canonical

	/**
	 * Collapse multiple spaces in string down to a single space. Remove lead
	 * and trailing spaces.
	 * 
	 * @param s
	 *            String to strip of blanks.
	 * 
	 * @return String with all blanks, lead/trail/embedded removed.
	 */
	public static String condense(String s) {
		if (s == null) {
			return null;
		}
		s = s.trim();
		if (s.indexOf("  ") < 0) {
			return s;
		}
		int len = s.length();
		// have to use StringBuffer for JDK 1.1
		StringBuffer b = new StringBuffer(len - 1);
		boolean suppressSpaces = false;
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c == ' ') {
				if (suppressSpaces) {
					// subsequent space
				} else {
					// first space
					b.append(c);
					suppressSpaces = true;
				}
			} else {
				// was not a space
				b.append(c);
				suppressSpaces = false;
			}
		} // end for
		return b.toString();
	} // end condense

	/**
	 * count of how many leading characters there are on a string matching a
	 * given character. It does not remove them.
	 * 
	 * @param text
	 *            text with possible leading characters, possibly empty, but not
	 *            null.
	 * @param c
	 *            the leading character of interest, usually ' ' or '\n'
	 * 
	 * @return count of leading matching characters, possibly 0.
	 */
	public static int countLeading(String text, char c) {
		// need defined outside the for loop.
		int count;
		for (count = 0; count < text.length() && text.charAt(count) == c; count++) {
		}
		return count;
	}

	/**
	 * count of how many trailing characters there are on a string matching a
	 * given character. It does not remove them.
	 * 
	 * @param text
	 *            text with possible trailing characters, possibly empty, but
	 *            not null.
	 * @param c
	 *            the trailing character of interest, usually ' ' or '\n'
	 * 
	 * @return count of trailing matching characters, possibly 0.
	 */
	public static int countTrailing(String text, char c) {
		int length = text.length();
		// need defined outside the for loop.
		int count;
		for (count = 0; count < length && text.charAt(length - 1 - count) == c; count++) {
		}
		return count;
	}

	/**
	 * Is this string empty?
	 * 
	 * @param s
	 *            String to be tested for emptiness.
	 * 
	 * @return true if the string is null or equal to the "" null string. or
	 *         just blanks
	 */
	public static boolean isEmptyTrim(String s) {
		return (s == null) || s.trim().length() == 0;
	} // end isEmpty

	/**
	 * Ensure the string contains only legal characters
	 * 
	 * @param candidate
	 *            string to test.
	 * @param legalChars
	 *            characters than are legal for candidate.
	 * 
	 * @return true if candidate is formed only of chars from the legal set.
	 */
	public static boolean isLegal(String candidate, String legalChars) {
		for (int i = 0; i < candidate.length(); i++) {
			if (legalChars.indexOf(candidate.charAt(i)) < 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if char is plain ASCII lower case.
	 * 
	 * @param c
	 *            char to check
	 * 
	 * @return true if char is in range a..z
	 * 
	 * @see Character#isLowerCase(char)
	 */
	public static boolean isUnaccentedLowerCase(char c) {
		return 'a' <= c && c <= 'z';
	} // isUnaccentedLowerCase

	/**
	 * Check if char is plain ASCII upper case.
	 * 
	 * @param c
	 *            char to check.
	 * 
	 * @return true if char is in range A..Z.
	 * 
	 * @see Character#isUpperCase(char)
	 */
	public static boolean isUnaccentedUpperCase(char c) {
		return 'A' <= c && c <= 'Z';
	} // end isUnaccentedUpperCase

	/**
	 * Pads the string out to the given length by applying blanks on the left.
	 * 
	 * @param s
	 *            String to be padded/chopped.
	 * @param newLen
	 *            length of new String desired.
	 * @param chop
	 *            true if Strings longer than newLen should be truncated to
	 *            newLen chars.
	 * 
	 * @return String padded on left/chopped to the desired length.
	 */
	public static String leftPad(String s, int newLen, boolean chop) {
		int grow = newLen - s.length();
		if (grow <= 0) {
			if (chop) {
				return s.substring(0, newLen);
			} else {
				return s;
			}
		} else if (grow <= 30) {
			return "                              ".substring(0, grow) + s;
		} else {
			return rep(' ', grow) + s;
		}
	} // end leftPad

	/**
	 * convert a String to a long. The routine is very forgiving. It ignores
	 * invalid chars, lead trail, embedded spaces, decimal points etc. Dash is
	 * treated as a minus sign.
	 * 
	 * @param numStr
	 *            String to be parsed.
	 * 
	 * @return long value of String with junk characters stripped.
	 * 
	 * @throws NumberFormatException
	 *             if the number is too big to fit in a long.
	 */
	public static long parseDirtyLong(String numStr) {
		numStr = numStr.trim();
		// strip commas, spaces, decimals + etc
		StringBuffer b = new StringBuffer(numStr.length());
		boolean negative = false;
		for (int i = 0; i < numStr.length(); i++) {
			char c = numStr.charAt(i);
			if (c == '-') {
				negative = true;
			} else if ('0' <= c && c <= '9') {
				b.append(c);
			}
		} // end for
		numStr = b.toString();
		if (numStr.length() == 0) {
			return 0;
		}
		long num = Long.parseLong(numStr);
		if (negative) {
			num = -num;
		}
		return num;
	} // end parseDirtyLong

	/**
	 * convert a String into long pennies. It ignores invalid chars, lead trail,
	 * embedded spaces. Dash is treated as a minus sign. 0 or 2 decimal places
	 * are permitted.
	 * 
	 * @param numStr
	 *            String to be parsed.
	 * 
	 * @return long pennies.
	 * 
	 * @throws NumberFormatException
	 *             if the number is too big to fit in a long.
	 */
	public static long parseLongPennies(String numStr) {
		numStr = numStr.trim();
		// strip commas, spaces, + etc
		StringBuffer b = new StringBuffer(numStr.length());
		boolean negative = false;
		int decpl = -1;
		for (int i = 0; i < numStr.length(); i++) {
			char c = numStr.charAt(i);
			switch (c) {
			case '-':
				negative = true;
				break;

			case '.':
				if (decpl == -1) {
					decpl = 0;
				} else {
					throw new NumberFormatException(
							"more than one decimal point");
				}
				break;

			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				if (decpl != -1) {
					decpl++;
				}
				b.append(c);
				break;

			default:
				// ignore junk chars
				break;
			} // end switch
		} // end for
		if (numStr.length() != b.length()) {
			numStr = b.toString();
		}

		if (numStr.length() == 0) {
			return 0;
		}
		long num = Long.parseLong(numStr);
		if (decpl == -1 || decpl == 0) {
			num *= 100;
		} else if (decpl == 2) { /* it is fine as is */
		}

		else {
			throw new NumberFormatException("wrong number of decimal places.");
		}

		if (negative) {
			num = -num;
		}
		return num;
	} // end parseLongPennies

	/**
	 * Print dollar currency, stored internally as scaled int. convert pennies
	 * to a string with a decorative decimal point.
	 * 
	 * @param pennies
	 *            long amount in pennies.
	 * 
	 * @return amount with decorative decimal point, but no lead $.
	 */
	public static String penniesToString(long pennies) {
		boolean negative;
		if (pennies < 0) {
			pennies = -pennies;
			negative = true;
		} else {
			negative = false;
		}
		String s = Long.toString(pennies);
		int len = s.length();
		switch (len) {
		case 1:
			s = "0.0" + s;
			break;
		case 2:
			s = "0." + s;
			break;
		default:
			s = s.substring(0, len - 2) + "." + s.substring(len - 2, len);
			break;
		} // end switch
		if (negative) {
			s = "-" + s;
		}
		return s;
	} // end penniesToString

	/**
	 * Produce a String of a given repeating character.
	 * 
	 * @param c
	 *            the character to repeat
	 * @param count
	 *            the number of times to repeat
	 * 
	 * @return String, e.g. rep('*',4) returns "****"
	 */
	public static String rep(char c, int count) {
		if (count <= 0) return "";
		char[] s = new char[count];
		for (int i = 0; i < count; i++) {
			s[i] = c;
		}
		return new String(s).intern();
	} // end rep

	/**
	 * Pads the string out to the given length by applying blanks on the right.
	 * 
	 * @param s
	 *            String to be padded/chopped.
	 * @param newLen
	 *            length of new String desired.
	 * @param chop
	 *            true if Strings longer than newLen should be truncated to
	 *            newLen chars.
	 * 
	 * @return String padded on left/chopped to the desired length.
	 */
	public static String rightPad(String s, int newLen, boolean chop) {
		int grow = newLen - s.length();
		if (grow <= 0) {
			if (chop) {
				return s.substring(0, newLen);
			} else {
				return s;
			}
		} else if (grow <= 30) {
			return s + "                              ".substring(0, grow);
		} else {
			return s + rep(' ', grow);
		}
	} // end rightPad

	/**
	 * Remove all spaces from a String.
	 * 
	 * @param s
	 *            String to strip of blanks.
	 * 
	 * @return String with all blanks, lead/trail/embedded removed.
	 */
	public static String squish(String s) {
		if (s == null) {
			return null;
		}
		s = s.trim();
		if (s.indexOf(' ') < 0) {
			return s;
		}
		int len = s.length();
		// have to use StringBuffer for JDK 1.1
		StringBuffer b = new StringBuffer(len - 1);
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c != ' ') {
				b.append(c);
			}
		} // end for
		return b.toString();
	} // end squish

	/**
	 * convert to Book Title case, with first letter of each word capitalised.
	 * e.g. "handbook to HIGHER consciousness" -> "Handbook to Higher
	 * Consciousness" e.g. "THE HISTORY OF THE U.S.A." -> "The History of the
	 * U.S.A." e.g. "THE HISTORY OF THE USA" -> "The History of the Usa" (sorry
	 * about that.) Don't confuse this with Character.isTitleCase which concerns
	 * ligatures.
	 * 
	 * @param s
	 *            String to convert. May be any mixture of case.
	 * 
	 * @return String with each word capitalised, except embedded words "the"
	 *         "of" "to"
	 */
	public static String toBookTitleCase(String s) {
		char[] ca = s.toCharArray();
		// Track if we changed anything so that
		// we can avoid creating a duplicate String
		// object if the String is already in Title case.
		boolean changed = false;
		boolean capitalise = true;
		boolean firstCap = true;
		for (int i = 0; i < ca.length; i++) {
			char oldLetter = ca[i];
			if (oldLetter <= '/' || ':' <= oldLetter && oldLetter <= '?'
					|| ']' <= oldLetter && oldLetter <= '`') {
				/* whitespace, control chars or punctuation */
				/* Next normal char should be capitalised */
				capitalise = true;
			} else {
				if (capitalise && !firstCap) {
					// might be the_ of_ or to_
					capitalise = !(s.substring(i, Math.min(i + 4, s.length()))
							.equalsIgnoreCase("the ")
							|| s.substring(i, Math.min(i + 3, s.length()))
									.equalsIgnoreCase("of ") || s.substring(i,
							Math.min(i + 3, s.length()))
							.equalsIgnoreCase("to "));
				} // end if
				char newLetter = capitalise ? Character.toUpperCase(oldLetter)
						: Character.toLowerCase(oldLetter);
				ca[i] = newLetter;
				changed |= (newLetter != oldLetter);
				capitalise = false;
				firstCap = false;
			} // end if
		} // end for
		if (changed) {
			s = new String(ca);
		}
		return s;
	} // end toBookTitleCase

	/**
	 * Convert int to hex with lead zeroes
	 * 
	 * @param h
	 *            number you want to convert to hex
	 * 
	 * @return 0x followed by unsigned hex 8-digit representation
	 * 
	 * @see #toString(Color)
	 */
	public static String toHexString(int h) {
		String s = Integer.toHexString(h);
		if (s.length() < 8) { // pad on left with zeros
			s = "00000000".substring(0, 8 - s.length()) + s;
		}
		return "0x" + s;
	}

	/**
	 * Convert an integer to a String, with left zeroes.
	 * 
	 * @param i
	 *            the integer to be converted
	 * @param len
	 *            the length of the resulting string. Warning. It will chop the
	 *            result on the left if it is too long.
	 * 
	 * @return String representation of the int e.g. 007
	 */
	public static String toLZ(int i, int len) {
		// Since String is final, we could not add this method there.
		String s = Integer.toString(i);
		if (s.length() > len) { /* return rightmost len chars */
			return s.substring(s.length() - len);
		} else if (s.length() < len)
		// pad on left with zeros
		{
			return "000000000000000000000000000000".substring(0, len
					- s.length())
					+ s;
		} else {
			return s;
		}
	} // end toLZ

	/**
	 * Get #ffffff html hex number for a colour
	 * 
	 * @param c
	 *            Color object whose html colour number you want as a string
	 * 
	 * @return # followed by 6 hex digits
	 * 
	 * @see #toHexString(int)
	 */
	public static String toString(Color c) {
		String s = Integer.toHexString(c.getRGB() & 0xffffff);
		if (s.length() < 6) { // pad on left with zeros
			s = "000000".substring(0, 6 - s.length()) + s;
		}
		return '#' + s;
	}

	/**
	 * Removes white space from beginning this string. <p/> All characters that
	 * have codes less than or equal to <code>'&#92;u0020'</code> (the space character) are
	 * considered to be white space.
	 * 
	 * @param s
	 *            String to process. As always the original in unchanged.
	 * 
	 * @return this string, with leading white space removed
	 */
	public static String trimLeading(String s) {
		if (s == null) {
			return null;
		}
		int len = s.length();
		int st = 0;
		while ((st < len) && (s.charAt(st) <= ' ')) {
			st++;
		}
		return (st > 0) ? s.substring(st, len) : s;
	} // end trimLeading

	/**
	 * Removes white space from end this string. <p/> All characters that have
	 * codes less than or equal to <code>'&#92;u0020'</code> (the space character) are considered
	 * to be white space.
	 * 
	 * @param s
	 *            String to process. As always the original in unchanged.
	 * 
	 * @return this string, with trailing white space removed
	 */
	public static String trimTrailing(String s) {
		if (s == null) {
			return null;
		}
		int len = s.length();
		int origLen = len;
		while ((len > 0) && (s.charAt(len - 1) <= ' ')) {
			len--;
		}
		return (len != origLen) ? s.substring(0, len) : s;
	} // end trimTrailing

	public static boolean equals(String in1, String in2) {

		return (in1 == null && in2 == null) || (in1 != null && in1.equals(in2));
	}

	/**
	 * Split the string in n parts and return the part number nr. If the part is not found
	 * the default value will be returned.
	 * @param value 
	 * @param nr 
	 * @param name
	 * @param def
	 * @param index
	 * @param subset
	 * @return
	 */
	public static String getPart(String value, int nr, String def) {
		return getPart(value, DEFAULT_SEPARATOR, nr, def);
	}

	public static String getPart(String value, int nr) {
		return getPart(value, DEFAULT_SEPARATOR, nr);
	}

	public static String getPart(String value, String separator, int nr, String def ) {
		String ret = getPart(value, separator, nr);
		return ret == null ? def : ret;
	}

	public static String getPart(String value, String separator, int nr ) {
		if (value == null)
			return null;
		String[] parts = value.split(separator);
		if (parts.length <= nr)
			return null;
		return parts[nr];
	}

	/**
	 * Return part before last occurrence of the char. If the char
	 * is not found it will return the hole string.
	 * 
	 * @param string
	 * @param c
	 * @return
	 */
	public static String beforeLastIndex(String string, char c) {
		if (string == null) return null;
		int pos = string.lastIndexOf(c);
		if (pos < 0) return string;
		return string.substring(0,pos);
	}

	/**
	 * Return the part after the last occurrence of the char. If the char
	 * is not found it will return the hole string.
	 * 
	 * @param string
	 * @param c
	 * @return
	 */
	public static String afterLastIndex(String string, char c) {
		if (string == null) return null;
		int pos = string.lastIndexOf(c);
		if (pos < 0) return string;
		return string.substring(pos+1);
	}
	
	/**
	 * Return part before last occurrence of the char. If the char
	 * is not found it will return the hole string.
	 * 
	 * @param string
	 * @param c
	 * @return
	 */
	public static String beforeLastIndex(String string, String c) {
		if (string == null) return null;
		int pos = string.lastIndexOf(c);
		if (pos < 0) return string;
		return string.substring(0,pos);
	}

	/**
	 * Return the part after the last occurrence of the char. If the char
	 * is not found it will return the hole string.
	 * 
	 * @param string
	 * @param c
	 * @return
	 */
	public static String afterLastIndex(String string, String c) {
		if (string == null) return null;
		int pos = string.lastIndexOf(c);
		if (pos < 0) return string;
		return string.substring(pos+1);
	}
	
	/**
	 * This will truncate the string at the end. If the string is shorter as length it returns
	 * the string itself.
	 * 
	 * @param in String to truncate
	 * @param length Max length of the string
	 * @return truncated string
	 */
	public static String truncate(String in, int length) {
		if (in.length() <= length) return in;
		return in.substring(0,length);
	}
	
	/**
	 * This will truncate the string, but it also adds three dots (...) at the removed position.
	 * The truncation is not at the end at all. The resulted string is a nice representation of
	 * the original input. Use this function to display a truncated string.
	 * 
	 * abc...xyz
	 * 
	 * @param in String to truncate
	 * @param length Max length of the string
	 * @return truncated string
	 */
	public static String truncateNice(String in, int length) {
		if (in.length() <= length) return in;
		if (length < 4) return in.substring(0,length);
		if (in.length() - length > 8)
			return in.substring(0,length - 8) + "..." + in.substring(in.length() - 5);
		return in.substring(0,length - 3) + "...";
	}
	
	/**
	 * Search in a string the selected paragraph indicated be empty lines. If the caret position outside
	 * the string it will return null.
	 * 
	 * @param text The string, must not be null
	 * @param caretPosition Position of the caret.
	 * @return the position or null if the input string is null. Return is int[] {start, end}
	 */
	public static int[] getSelectedPart(String text,int caretPosition) {
		return getSelectedPart(text, caretPosition, DEFAULT_SELECTION_SEPARATOR);
	}
	
	public static int[] getSelectedPart(String text,int caretPosition, String[] separators) {
		String s = text;
		if (s == null || caretPosition > text.length()) return null;
		int start = -1;
		int near  = -1;
		String usedSep = null;
		for (String separator : separators) {
			int cur = s.lastIndexOf(separator, caretPosition);
			if (cur+separator.length() > near) {
				near = cur+separator.length();
				start = cur;
				usedSep = separator;
			}
		}
		if (start < 0)
			start = 0;
		else
			start += usedSep.length();
		
		int end = Integer.MAX_VALUE;
		for (String separator : separators) {
			int cur = s.indexOf(separator, caretPosition);
			if (cur >= 0) end = Math.min(end, cur);
		}
		if (end < 0)
			end = s.length();
//		s = s.substring(start, end);
		return new int[] {start, end};
	}

	/**
	 * Returns the string part of the selected part. If the part position is 
	 * outside the bounds of the string it will be trimmed. If the part
	 * position is not correct or the returned string is empty then the default
	 * string is returned. Good values for the default are null or an empty string.
	 * 
	 * Use this method with the result of getSelectedPart()
	 * 
	 * @param text The text to crop
	 * @param part The position in the text, first element is the begin second element the end of the selection.
	 * @param def Return this if a selection is not possible.
	 * @return The selected part of the text.
	 */
	public static String getSelection(String text, int[] part, String def) {
		if (text == null || part == null || part.length < 2) return def;
		int start = part[0];
		int end = part[1];
		if (end > text.length()) end = text.length();
		if (start < 0) start = 0;
		if (start >= end) return def;
		return text.substring(start, end);
	}
	
	/**
	 * Replace a text in another text.
	 * 
	 * @param text
	 * @param part
	 * @param replacement
	 * @return
	 */
	public static String replaceSelection(String text, int[] part, String replacement) {
		if (text == null || part == null || part.length < 2 || replacement == null) return null;
		
		int start = part[0];
		int end = part[1];
		if (end > text.length()) end = text.length();
		if (start < 0) start = 0;
		if (start >= end) return null;
		
		return text.substring(0, start) + replacement + text.substring(end);
	}
	
	/**
	 * Append the number of times a character to a string buffer.
	 * 
	 * @param amount number of repeatings
	 * @param fill char to fill
	 * @param buffer target
	 */
	public static void appendRepeating(int amount, char fill, StringBuffer buffer) {
		for (int i = 0; i < amount; i++) buffer.append(fill);
	}
	
	/**
	 * Repeate the character a given times.
	 * 
	 * @param amount number of repeatings
	 * @param fill char to fill
	 * @return
	 */
	public static String getRepeatig(int amount, char fill) {
		StringBuffer buffer = new StringBuffer();
		appendRepeating(amount, fill, buffer);
		return buffer.toString();
	}
	
	/**
	 * Return true if the character is in the list of whitespaces.
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isWhitespace(char c) {
		for (char w : WHITESPACE)
			if (w == c) return true;
		return false;
	}
	
	/**
	 * Return the count of the character in the string.
	 * 
	 * @param in
	 * @param c
	 * @return count of chars or 0 if 'in' is null
	 */
	public static int countCharacters(String in, char c) {
		
		int res = 0;
		if (in == null) return 0;
		for (int i = 0; i < in.length(); i++) {
			if (in.charAt(i) == c) res++;
		}
		return res;
	}
	
	/**
	 * Return the count of the characters in the string.
	 * 
	 * @param in
	 * @param c
	 * @return count of chars or 0 if 'in' is null
	 */
	public static int countCharacters(String in, char[] c) {
		
		int res = 0;
		if (in == null || c == null) return 0;
		for (char cc : c) {
			res+=countCharacters(in,cc);
		}
		return res;
	}
	
	/**
	 * Try to find initials out of the given title. Use the first letters after whitspaces for the initials. The
	 * returned string is not in upper letters. If you need Upper Letters append toUpper().
	 * @param title The input String
	 * @param upperOnly Set true if only collect upper characters in title
	 * @param digits Set true if digits (after whitespaces) should also be used.
	 * @param maxSize Max size in characters of the initilas or 0 for no limit.
	 * @return The initials in mixed letters.
	 */
	public static String findInitials(String title, boolean upperOnly, boolean digits, int maxSize) {
		if (isEmptyTrim(title)) return "?";
		StringBuffer out = new StringBuffer();
		// find first letters
		boolean isSpace = true;
		for (int i = 0; i < title.length(); i++) {
			char c = title.charAt(i);
			if (isWhitespace(c)) {
				isSpace = true;
			} else {
				if (isSpace) {
					if ((c >= 'A' && c <= 'Z') || !upperOnly && ( c >= 'a' && c <= 'z') || digits && (c >= '0' && c <= '9'))
						out.append(c);
					if (maxSize > 0 && out.length() >= maxSize) break;
				}
				isSpace = false;
			}
		}
		return out.toString();
	}
	
	/**
	 * Check a string against all characters in another string. If the first string is not build from
	 * characters of the other one the method returns false.
	 * @param in
	 * @param composed
	 * @return
	 */
	public static boolean isComposedOf(String in, String composed) {
		if (in == null || composed == null) return false;
		for (int p = 0; p < in.length(); p++) {
			char c = in.charAt(p);
			if (composed.indexOf(c) < 0) return false;
		}
		return true;
	}

	public static String integerPart(String in) {
		if (in == null) return in;
		in = in.trim();
		for (int p = 0; p < in.length(); p++) {
			char c = in.charAt(p);
			if (! (c >= '0' && c <= '9' || (p == 0 && (c == '-' || c == '+') ) )  ) {
				if (p == 0) return null;
				return in.substring(0,p);
			}
		}
		return null;
	}

	public static String beforeIndexOrAll(String _s, char _c) {
		int p = _s.indexOf(_c);
		if (p < 0)
			return _s;

		return _s.substring(0, p);
	}
}
