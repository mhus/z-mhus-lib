package de.mhus.lib.core.io;

import java.io.FileWriter;
import java.io.IOException;
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
 */
public class CSVWriter {

	// ------------------------------ FIELDS ------------------------------

	/**
	 * true if want debugging output
	 */
	static final boolean DEBUGGING = false;

	/**
	 * line separator to use. We use Windows style for all platforms since csv
	 * is a Windows format file.
	 */
	private static final String lineSeparator = "\r\n";

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
	 * true if there has was a field previously written to this line, meaning
	 * there is a comma pending to be written.
	 */
	private boolean wasPreviousField = false;

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

	/**
	 * Write a new line in the CVS output file to demark the end of record.
	 */
	public void nl() {
		if (pw == null) {
			throw new IllegalArgumentException(
					"attempt to use a closed CSVWriter");
		}
		/* don't bother to write last pending comma on the line */
		pw.write(lineSeparator);
		wasPreviousField = false;
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
	 * 
	 * @param s
	 *            The string to write. Any additional quotes or embedded quotes
	 *            will be provided by put. Null means start a new line.
	 */
	public void put(String ... values) {
		if (pw == null) {
			throw new IllegalArgumentException(
					"attempt to use a closed CSVWriter");
		}
		if (values == null || values.length == 0) return;
		
		for (String s : values ) {
			if (s == null) {
	
				if (defaultNullValue == null) {
					nl();
					return;
				}
				s = defaultNullValue;
			}
	
			if (wasPreviousField) {
				pw.write(separator);
			}
			if (trim) {
				s = s.trim();
			}
			if (s.indexOf(quote) >= 0) {
				/* worst case, needs surrounding quotes and internal quotes doubled */
				if (quote != CSVReader.NO_QUOTS) pw.write(quote);
				for (int i = 0; i < s.length(); i++) {
					char c = s.charAt(i);
					if (c == quote) {
						if (quote != CSVReader.NO_QUOTS) pw.write(quote);
						pw.write(quote);
					} else {
						pw.write(c);
					}
				}
				if (quote != CSVReader.NO_QUOTS) pw.write(quote);
			} else if (quoteLevel == 2 || quoteLevel == 1 && s.indexOf(' ') >= 0
					|| s.indexOf(separator) >= 0) {
				/* need surrounding quotes */
				if (quote != CSVReader.NO_QUOTS) pw.write(quote);
				pw.write(s);
				if (quote != CSVReader.NO_QUOTS) pw.write(quote);
			} else {
				/* ordinary case, no surrounding quotes needed */
				pw.write(s);
			}
			/* make a note to print trailing comma later */
			wasPreviousField = true;
		}
	}

	// --------------------------- main() method ---------------------------

	/**
	 * Test driver
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		if (DEBUGGING) {
			try {
				// write out a test file
				@SuppressWarnings("resource")
				CSVWriter csv = new CSVWriter(new FileWriter("temp.txt"), 1,
						';', '\'', true);
				csv.put("abc");
				csv.put("def");
				csv.put("g h i");
				csv.put("jk,l");
				csv.put("m\"n\'o ");
				csv.nl();
				csv.put("m\"n\'o ");
				csv.put("    ");
				csv.put("a");
				csv.put("x,y,z");
				csv.put("x;y;z");
				csv.nl();
				csv.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} // end if
	} // end main
} // end CSVWriter class.
