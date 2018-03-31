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
package de.mhus.lib.core.io;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * Write CSV (Comma Separated Value) files. This format is used my Microsoft
 * Word and Excel. Fields are separated by commas, and enclosed in quotes if
 * they contain commas or quotes. Embedded quotes are doubled. Embedded spaces
 * do not normally require surrounding quotes. The last field on the line is not
 * followed by a comma. Null fields are represented by two commas in a row.
 * 
 * @author copyright (c) 2002-2006 Roedy Green Canadian Mind Products version
 *         1.0 2002 March 27 <br>
 *         1.1 2002 March 28 - allow variable separator - add close method<br>
 *         1.2 2002 April 23 - put in to separate package<br>
 *         1.3 2002 April 24 - three levels of quoting <br>
 *         1.4 2002 April 24 - convenience constructor - put(null) now means nl.<br>
 *         1.6 2002 May 25 - allow choice of quote char <br>
 *         1.9 2002 November 14 - trim parameter to control whether fields are
 *         trimmed of lead/trail whitespace (blanks, Cr, Lf, Tab etc.) before
 *         writing.<br>
 *         2.1 2005-07-17 reorganization, new bat files.<br>
 *         2.2 2005-08-28 - add CSVAlign and CSVPack to the suite.
 *         2.3 2015-06-12 fix enclosing of fields
 */
public class CSVWriter {

	// ------------------------------ FIELDS ------------------------------

	/**
	 * line separator to use. We use Windows style for all platforms since csv
	 * is a Windows format file.
	 */
	private String lineSeparator = "\r\n";

	/**
	 * PrintWriter where CSV fields will be written.
	 */
	private PrintWriter pw;

	/**
	 * true if write should trim lead/trail whitespace from fields before
	 * writing them.
	 */
	private final boolean trim;

	/**
	 * next column to write, contains the current size of columns for actual line
	 */
	private int column = 0;
	
	/**
	 * quote character, usually '\"' '\'' for SOL used to enclose fields
	 * containing a separator character.
	 */
	private char quote;

	/**
	 * field separator character, usually ',' in North America, ';' in Europe
	 * and sometimes '\t' for tab.
	 */
	private char separator;

	/**
	 * how much extra quoting you want
	 */
	private int quoteLevel;

	private String defaultNullValue = "";
	
	private ValueWriter writer = new DefaultValueWriter();

	// --------------------------- CONSTRUCTORS ---------------------------

	/**
	 * convenience Constructor, defaults to quotelevel 1, comma separator , trim
	 * 
	 * @param pw
	 *            Writer where fields will be written.
	 */
	public CSVWriter(Writer pw) {
		this(pw, 1, ',', '\"', true);
	}

	/**
	 * Constructor
	 * 
	 * @param pw
	 *            Writer where fields will be written.
	 * @param quoteLevel
	 *            0 = minimal quotes <br>
	 *            1 = quotes also around fields containing spaces<br>
	 *            2 = quotes around all fields, whether or not they contain
	 *            commas, quotes or spaces.
	 * @param separator
	 *            field separator character, usually ',' in North America, ';'
	 *            in Europe and sometimes '\t' for tab.
	 * @param quote
	 *            char to use to enclose fields containing a separator, usually
	 *            '\"'
	 * @param trim
	 *            true if writer should trim leading/trailing whitespace (e.g.
	 *            blank, cr, Lf, tab) before writing the field.
	 */
	public CSVWriter(Writer pw, int quoteLevel, char separator, char quote,
			boolean trim) {
		// we want a PrintWriter not for its methods, but so that it won't throw
		// so many IOExceptions
		if (pw instanceof PrintWriter) {
			this.pw = (PrintWriter) pw;
		} else {
			this.pw = new PrintWriter(pw);
		}
		if (this.pw == null) {
			throw new IllegalArgumentException("invalid Writer");
		}
		this.quoteLevel = quoteLevel;
		this.separator = separator;
		this.quote = quote;
		this.trim = trim;
	}

	// -------------------------- OTHER METHODS --------------------------

	/**
	 * Close the PrintWriter.
	 */
	public void close() {
		if (pw != null) {
			pw.close();
			pw = null;
		}
	}

	/**
	 * Set default value if put() gets a value of null. If the defaultNullValue
	 * is also null put() will write a new line. Default is a empty string.
	 * 
	 * @param in
	 */
	public void setDefaultNullValue(String in) {
		defaultNullValue = in;
	}
	
	public String getDefaultNullValue() {
		return defaultNullValue;
	}

	/**
	 * Write a new line in the CVS output file to demark the end of record.
	 */
	public void nl() {
		if (pw == null) {
			throw new IllegalArgumentException(
					"attempt to use a closed CSVWriter");
		}
		/* don't bother to write last pending comma on the line */
		writer.nl(this);
		column = 0;
	}

	public void print(Object ... values) {
		String[] v = new String[values.length];
		for (int i = 0; i < values.length; i++)
			v[i] = String.valueOf(values[i]);
		put(v);
	}
	
	public void println(Object ... values) {
		String[] v = new String[values.length];
		for (int i = 0; i < values.length; i++)
			v[i] = String.valueOf(values[i]);
		put(v);
		nl();
	}
	
	/**
	 * Write one csv field to the file, followed by a separator unless it is the
	 * last field on the line. Lead and trailing blanks will be removed.
	 * @param values 
	 */
	public void put(String ... values) {
		if (pw == null) {
			throw new IllegalArgumentException(
					"attempt to use a closed CSVWriter");
		}
		if (values == null || values.length == 0) return;
		
		for (String s : values ) {
			
			
			if (column != 0) {
				pw.write(separator);
			}
			writer.write(this, s);
			
			/* make a note to print trailing comma later */
			column++;
		}
	}

	public String getLineSeparator() {
		return lineSeparator;
	}

	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}
	
	public boolean isTrim() {
		return trim;
	}
	
	public char getQuote() {
		return quote;
	}

	public PrintWriter getPrintWriter() {
		return pw;
	}
	
	public int getQuoteLevel() {
		return quoteLevel;
	}
	
	public int getColumnOfLine() {
		return column;
	}
	
	public ValueWriter getValueWriter() {
		return writer;
	}

	public void setValueWriter(ValueWriter writer) {
		this.writer = writer;
	}

	public static interface ValueWriter {
		void write(CSVWriter writer, String value);
		void nl(CSVWriter writer);
	}
	
	public static class DefaultValueWriter implements ValueWriter {

		@Override
		public void write(CSVWriter writer, String s) {
			
			PrintWriter pw = writer.getPrintWriter();
			int quoteLevel = getQuoteLevel(writer);
			char quote = writer.getQuote();
			
			if (s == null) {
				
				if (writer.getDefaultNullValue() == null) {
					writer.nl();
					return;
				}
				s = writer.getDefaultNullValue();
			}
			
			if (writer.isTrim()) {
				s = s.trim();
			}
			if (s.indexOf(quote) >= 0) {
				/* worst case, needs surrounding quotes and internal quotes doubled */
				if (quote != CSVReader.NO_QUOTS) pw.write(quote);
				for (int i = 0; i < s.length(); i++) {
					char c = s.charAt(i);
					if (c == quote) {
						if (quote != CSVReader.NO_QUOTS) pw.write(quote);
						pw.write(writer.getQuote());
					} else {
						pw.write(c);
					}
				}
				if (quote != CSVReader.NO_QUOTS) pw.write(writer.getQuote());
			} else if (
					quoteLevel == 2 || 
					quoteLevel == 1 && s.indexOf(' ') >= 0 || 
					s.indexOf(writer.getLineSeparator()) >= 0)
			{
				/* need surrounding quotes */
				if (quote != CSVReader.NO_QUOTS) pw.write(quote);
				pw.write(s);
				if (quote != CSVReader.NO_QUOTS) pw.write(quote);
			} else {
				/* ordinary case, no surrounding quotes needed */
				pw.write(s);
			}

			
		}
		
		public int getQuoteLevel(CSVWriter writer) {
			return writer.getQuoteLevel();
		}
		
		@Override
		public void nl(CSVWriter writer) {
			writer.getPrintWriter().write(writer.getLineSeparator());

		}
		
	}
	
	/*
	 * 
	 * writer = new CSVWriter(...);
	 * writer.setValueWriter(new CSVWriter.DefaultValueWriter() {
	 *   public void write(CSVWriter writer, String value) {
	 *     if (MString.isEmpty(value)) return;
	 *     super(write(writer,value);
	 *   }
	 * }
	 * 
	 */

} // end CSVWriter class.
