package de.mhus.lib.core.parser;

public interface Parser {

	public CompiledString compileString(String in) throws ParseException;

}
