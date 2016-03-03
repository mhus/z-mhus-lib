package de.mhus.lib.core.parser;

/**
 * <p>TechnicalStringParser class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class TechnicalStringParser extends StringTokenizerParser {

	/**
	 * <p>Constructor for TechnicalStringParser.</p>
	 *
	 * @param condition a {@link java.lang.String} object.
	 */
	public TechnicalStringParser(String condition) {
		super(condition);
		breakableCharacters = "{}[]()!=<>*+-";
		enclosureCharacters = "\"'";
		whiteSpace = " \r\t";
		encapsulateCharacters = "\\";
		lineSeparator = "\n";
	}

}
