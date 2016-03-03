package de.mhus.lib.core.parser;

/**
 * <p>CsvStringParser class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class CsvStringParser extends StringTokenizerParser {

	/**
	 * <p>Constructor for CsvStringParser.</p>
	 *
	 * @param condition a {@link java.lang.String} object.
	 */
	public CsvStringParser(String condition) {
		super(condition);
		breakableCharacters = ",";
		enclosureCharacters = "\"";
		whiteSpace = " \r";
		encapsulateCharacters = "\"";
		lineSeparator = "\n";
	}

}
