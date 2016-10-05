package de.mhus.lib.core.parser;


public interface ParsingPart extends StringPart {

	void parse(ParseReader str) throws ParseException;
	
}
