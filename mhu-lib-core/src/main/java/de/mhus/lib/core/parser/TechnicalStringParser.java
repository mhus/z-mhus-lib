package de.mhus.lib.core.parser;

public class TechnicalStringParser extends StringTokenizerParser {

	public TechnicalStringParser(String condition) {
		super(condition);
		breakableCharacters = "{}[]()!=<>*+-";
		enclosureCharacters = "\"'";
		whiteSpace = " \r\t";
		encapsulateCharacters = "\\";
		lineSeparator = "\n";
	}

}
