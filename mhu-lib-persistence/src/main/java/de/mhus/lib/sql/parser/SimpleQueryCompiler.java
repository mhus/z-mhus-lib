package de.mhus.lib.sql.parser;

import java.util.Date;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MSql;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.ParsingPart;
import de.mhus.lib.core.parser.StringCompiler;
import de.mhus.lib.core.parser.StringPart;

public class SimpleQueryCompiler extends StringCompiler implements ICompiler {

	//	private static Log log = Log.getLog(SimpleQueryCompiler.class);

	@Override
	protected StringPart createDefaultAttributePart(String part) {
		ParameterPart out = new ParameterPart(this);
		out.attribute = MString.split(part,",");
		return out;
	}

	@Override
	public boolean isParseAttributes() {
		return true;
	}

	@Override
	public ParsingPart compileFunction(FunctionPart function) {
		return function;
	}

	@Override
	public String toSqlDateValue(Date string) {
		return "'" + MCast.toString(string) + "'";
	}

	@Override
	public String valueToString(Object value) {
		return MCast.objectToString(value);
	}

	@Override
	public String escape(String text) {
		return MSql.escape(text);
	}


}