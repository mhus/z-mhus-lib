/*
 * ./xml/de/mhu/lib/xml/parser/Parser.java
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

package de.mhus.lib.core.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;
import java.util.Map;

import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;

/**
 * <p>HtmlParser class.</p>
 *
 * @author hummel
 * @version $Id: $Id
 */
public class HtmlParser {

	/** Constant <code>TEXT=1</code> */
	public static final int TEXT = 1;
	/** Constant <code>TAG=2</code> */
	public static final int TAG = 2;

	private Reader is = null;
	private Listener listener = null;
	private boolean trim = false;

	/**
	 * <p>Constructor for HtmlParser.</p>
	 */
	public HtmlParser() {
	}

		/**
		 * Creates a new instance of Parser
		 *
		 * @param _is a {@link java.io.InputStream} object.
		 * @param _listener a {@link de.mhus.lib.core.parser.HtmlParser.Listener} object.
		 */
	public HtmlParser(InputStream _is, Listener _listener) {
		is = new InputStreamReader(_is);
		listener = _listener;
	}

	public HtmlParser(Reader _is, Listener _listener) {
		is = _is;
		/**
		 * <p>Constructor for HtmlParser.</p>
		 *
		 * @param _is a {@link java.io.Reader} object.
		 * @param _listener a {@link de.mhus.lib.core.parser.HtmlParser.Listener} object.
		 */
		listener = _listener;
	}

	public boolean parse(Reader _is, Listener _listener) {
		is = _is;
		/**
		 * <p>parse.</p>
		 *
		 * @param _is a {@link java.io.Reader} object.
		 * @param _listener a {@link de.mhus.lib.core.parser.HtmlParser.Listener} object.
		 * @return a boolean.
		 */
		listener = _listener;
		return parse();
	}

	public boolean parse() {

		/**
		 * <p>parse.</p>
		 *
		 * @return a boolean.
		 */
		int type = TEXT;
		StringBuffer text = new StringBuffer();
		char[] one = new char[1];

		try {
			while (true) {

				int i = is.read(one);
				if (i != 1)
					break;

				char c = one[0];
				// System.out.println( "Char: " + c );

				switch (type) {
				case TEXT:
					if (c == '<') {
						type = TAG;
						trim(text);
						if (text.length() > 0 && !listener
								.foundText(MXml.decode(text.toString())))
							return false;
						text = new StringBuffer();
					} else {
						text.append(c);
					}
					break;
				case TAG:
					if (c == '>') {
						type = TEXT;
						String tag = text.toString().trim();
						// System.out.println( "Tag: [" + tag + "] Length: " +
						// tag.length() );
						if (tag.length() > 0) {
							if (tag.startsWith("!--") && tag.endsWith("--")) {
								// System.out.println( "Send Note: " +
								// tag.substring( 3, tag.length()-2 ) );
								if (!listener.foundNote(tag.substring(3, tag
										.length() - 2)))
									return false;
							} else if (tag.startsWith("?") && tag.endsWith("?")) {
								if (!listener.foundProcessorInstruction(tag.substring(1,
										tag.length() - 1).trim()))
									return false;
							} else if (tag.startsWith("/")) {
								if (!listener.foundTagClose(tag.substring(1)
										.trim()))
									return false;
							} else if (tag.endsWith("/")) {
								String tagx = tag
										.substring(0, tag.length() - 1).trim();
								if (!listener.foundSingleTag(getTagName(tagx),
										getTagParams(tagx)))
									return false;
							} else {
								if (!listener.foundTagOpen(getTagName(tag),
										getTagParams(tag)))
									return false;
							}
						} else {
							System.err.println("Tag without name");
							return true;
						}
						text = new StringBuffer();
					} else {
						text.append(c);
					}
					break;
				}

			}

			if (type == TEXT) {
				trim(text);
				if (text.length() > 0 && !listener.foundText(MXml.decode(text.toString())))
					return false;
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
			return true;
		}

		return true;
	}

	protected void trim(StringBuffer text) {
		if (!trim) return;
		/**
		 * <p>trim.</p>
		 *
		 * @param text a {@link java.lang.StringBuffer} object.
		 */
		while (text.length() > 0 && MString.isWhitespace(text.charAt(0)) )
			text.deleteCharAt(0);
		while (text.length() > 0 && MString.isWhitespace(text.charAt(text.length()-1)) )
			text.deleteCharAt(text.length()-1);
	}

	/**
	 * @param tag
	 * @return
	 */
	private String getTagName(String tag) {
		if (MString.isIndex(tag, MString.WHITESPACE)) {
			return MString.beforeIndex(tag, MString.WHITESPACE);
		}
		return tag;

	}

	/**
	 * @param tag
	 */
	private Hashtable<String,String> getTagParams(String tag) {
		Hashtable<String,String> out = new Hashtable<String,String>();

		if (!MString.isIndex(tag, MString.WHITESPACE)) {
			return out;
		}

		String tagx = MString.afterIndex(tag, MString.WHITESPACE);

		while (tagx.length() != 0) {
			int pos = tagx.indexOf('=');
			if (pos < 0)
				return out;

			String key = tagx.substring(0, pos).trim();
			tagx = tagx.substring(pos + 1);

			int pos1 = tagx.indexOf('"');
			int pos2 = tagx.indexOf('\'');
			
			if (pos1 < 0) pos = pos2;
			else
			if (pos2 < 0) pos = pos1;
			else
			if (pos2 < pos1) pos = pos2;
			else
				pos = pos1;
				
			if (pos < 0)
				return out;
			tagx = tagx.substring(pos + 1);

			pos = tagx.indexOf(pos == pos1 ? '"' : '\'');
			if (pos < 0)
				return out;
			out
					.put(MXml.decode(key), MXml.decode(tagx.substring(0,
							pos)));
			tagx = tagx.substring(pos + 1);
		}

		return out;

	}

	public boolean isTrim() {
		return trim;
	/**
	 * <p>isTrim.</p>
	 *
	 * @return a boolean.
	 */
	}

	public void setTrim(boolean trim) {
		this.trim = trim;
	/**
	 * <p>Setter for the field <code>trim</code>.</p>
	 *
	 * @param trim a boolean.
	 */
	}

	public interface Listener {

		public boolean foundText(String text);

		public boolean foundNote(String note);

		public boolean foundProcessorInstruction(String pi);

		public boolean foundTagClose(String name);

		public boolean foundSingleTag(String name, Map<String,String> _params);

		public boolean foundTagOpen(String name, Map<String,String> _params);

	}

}
