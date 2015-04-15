package de.mhus.lib.sql.parser;

import java.util.Date;

import de.mhus.lib.core.parser.ParsingPart;

public interface ICompiler {

	boolean isParseAttributes();

	ParsingPart compileFunction(FunctionPart function);

	String toSqlDateValue(Date string);

	String valueToString(Object value);

	String escape(String text);

}
