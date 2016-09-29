package de.mhus.lib.core.parser;

public class CsvStringParser extends StringTokenizerParser {

	public CsvStringParser(String condition) {
		super(condition);
		breakableCharacters = ",";
		enclosureCharacters = "\"";
		whiteSpace = " \r";
		encapsulateCharacters = "\"";
		lineSeparator = "\n";
	}

}
