/*
 * ./core/de/mhu/lib/Rfc1738.java
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

package de.mhus.lib.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;

/**
 * The class encodes/decodes strings in rfc1738 format.
 *
 * @author jesus
 * @version $Id: $Id
 */
public class Rfc1738 extends TreeMap<String,String> {


	/**
	 * serial ID
	 */
	private static final long serialVersionUID = -3321101084525120985L;

	/**
	 * <p>put.</p>
	 *
	 * @param _key a {@link java.lang.String} object.
	 * @param _value a int.
	 */
	public void put(String _key, int _value) {
		put(_key, MCast.toString(_value));
	}

	/**
	 * <p>getInt.</p>
	 *
	 * @param _key a {@link java.lang.String} object.
	 * @param _def a int.
	 * @return a int.
	 */
	public int getInt(String _key, int _def) {
		String v = get(_key);
		if (v == null)
			return _def;
		return MCast.toint(v,0);
	}

	/**
	 * <p>getInt.</p>
	 *
	 * @param _key a {@link java.lang.String} object.
	 * @return a int.
	 */
	public int getInt(String _key) {
		return getInt(_key, -1);
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return implode(this);
	}

	/**
	 * Decode a string with rfc1738 spec.
	 *
	 * @param _in encoded string
	 * @return decoded string
	 */
	public static String decode(String _in) {

		if (_in == null)
			return "";

		try {
			return URLDecoder.decode(_in, MString.CHARSET_DEFAULT);
		} catch (UnsupportedEncodingException e) {
			// TODO log it
		}
		
		StringBuffer sb = new StringBuffer();

		int mode = 0;
		int buffer = 0;
		for (int i = 0; i < _in.length(); i++) {

			char c = _in.charAt(i);

			if (mode == 0) {

				if (c == '%') {
					mode = 1;
					buffer = 0;
				} else if (c == '+')
					sb.append(' ');
				else
					sb.append(c);

			} else if (mode == 1) {

				if (c >= '0' && c <= '9')
					buffer = c - '0';
				else if (c >= 'A' && c <= 'F')
					buffer = c + 10 - 'A';
				else if (c >= 'a' && c <= 'f')
					buffer = c + 10 - 'a';

				mode = 2;
			} else if (mode == 2) {

				buffer = buffer * 16;

				if (c >= '0' && c <= '9')
					buffer += c - '0';
				else if (c >= 'A' && c <= 'F')
					buffer += c + 10 - 'A';
				else if (c >= 'a' && c <= 'f')
					buffer += c + 10 - 'a';

				sb.append((char) buffer);
				mode = 0;
			}

		}

		return sb.toString();
	}

	/**
	 * encode a string in rfc1738 spec
	 *
	 * @param _in decoded string
	 * @return encoded string
	 */
	public static String encode(String _in) {
		if (_in == null) return "";
		try {
			return URLEncoder.encode(_in,MString.CHARSET_DEFAULT); // as default charset
		} catch (UnsupportedEncodingException e) {
			//TODO log
		}

		return encodeNoUTF8(_in);
	}
	
	/**
	 * <p>encodeNoUTF8.</p>
	 *
	 * @param _in a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String encodeNoUTF8(String _in) {
	
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < _in.length(); i++) {

			char c = _in.charAt(i);

//			if (c == '%' || c == '&' || c == '=' || c == '+' || c == '\n'
//					|| c == '\r' || c == '?' || c == ' ' )
			if (! (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9' || c == ',' || c == '.' ) ) {

				encodeNoUTF8(sb, c);
				
			}
			else
				sb.append(c);
		}

		return sb.toString();

	}

	/**
	 * <p>encode.</p>
	 *
	 * @param c a char.
	 * @return a {@link java.lang.String} object.
	 */
	public static String encode(char c) {
		StringBuffer sb = new StringBuffer();
		encodeNoUTF8(sb, c);
		return sb.toString();
	}
	
	/**
	 * <p>encodeNoUTF8.</p>
	 *
	 * @param sb a {@link java.lang.StringBuffer} object.
	 * @param c a char.
	 */
	public static void encodeNoUTF8(StringBuffer sb, char c) {
		
		if (c == ' ' ) {
			sb.append('+');
			return;
		}
		
		sb.append('%');

		int buffer = 0;

		int cc = c;
		buffer = cc / 16;
		if (buffer < 10)
			sb.append((char) ((int) '0' + buffer));
		else
			sb.append((char) ((int) 'A' - 10 + buffer));

		buffer = cc % 16;
		if (buffer < 10)
			sb.append((char) ((int) '0' + buffer));
		else
			sb.append((char) ((int) 'A' - 10 + buffer));

	}
	
	/**
	 * Transfor the elements of an array to a string using the rfc1738 sprec.
	 *
	 * @param in an array of {@link java.lang.String} objects.
	 * @return a {@link java.lang.String} object.
	 */
	public static String implodeArray(String[] in) {
		
		if (in==null) return "";
		
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for ( String i : in ) {
			if (!first)
				sb.append('&');
			sb.append(encode(i));
			first = false;
		}
		return sb.toString();
		
	}
	
	/**
	 * Transforms a encoded array of strings back.
	 *
	 * @param in a {@link java.lang.String} object.
	 * @return an array of {@link java.lang.String} objects.
	 */
	public static String[] explodeArray(String in) {
		
		if ( in == null || in.length()==0) return new String[0];
		
		String[] out = in.split("&");
		for (int i = 0; i < out.length; i++)
			out[i]=decode(out[i]);
		
		return out;
	}
	
	/**
	 * Transforms a list encoded map of attributes back.
	 *
	 * @param _in a {@link java.lang.String} object.
	 * @return a {@link java.util.Map} object.
	 */
	public static Map<String,String> explode(String _in) {

		if (_in == null)
			return new TreeMap<String,String>();

		TreeMap<String,String> out = new TreeMap<String,String>();

		String[] obj = _in.split("&");

		for (int i = 0; i < obj.length; i++) {

			String[] kv = obj[i].split("=");

			if (kv.length == 2) {
				if (kv[1] == null)
					kv[1] = "";
				out.put(decode(kv[0]), decode(kv[1]));
			} else if (kv.length == 1)
				out.put(decode(kv[0]), "");

		}

		return out;
	}

	/**
	 * Encode a list of attributes in a single string
	 *
	 * @param _in a {@link java.util.Map} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String implode(Map<String,String> _in) {

		if (_in == null)
			return "";

		StringBuffer sb = new StringBuffer();

		boolean first = true;

		for (Iterator<String> e = _in.keySet().iterator(); e.hasNext();) {

			String key = e.next();
			String value = _in.get(key);

			if (value != null) {
				if (!first)
					sb.append('&');
				sb.append(encode(key));
				sb.append('=');
				sb.append(encode(value));
				first = false;
			}

		}

		return sb.toString();
	}

	/**
	 * Encode a list of attributes in a single string
	 *
	 * @param _in a {@link de.mhus.lib.core.IProperties} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String implode(IProperties _in) {

		if (_in == null)
			return "";

		StringBuffer sb = new StringBuffer();

		boolean first = true;

		for (Iterator<String> e = _in.keys().iterator(); e.hasNext();) {

			String key = e.next();
			String value = _in.getString(key, null);

			if (value != null) {
				if (!first)
					sb.append('&');
				sb.append(encode(key));
				sb.append('=');
				sb.append(encode(value));
				first = false;
			}

		}

		return sb.toString();
	}
	
}
