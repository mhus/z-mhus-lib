package de.mhus.lib.sql.parser;

import java.util.Date;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MSql;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.parser.ParsingPart;
import de.mhus.lib.core.parser.StringCompiler;
import de.mhus.lib.core.parser.StringPart;

/**
 * <p>SimpleQueryCompiler class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class SimpleQueryCompiler extends StringCompiler implements ICompiler {

	//	private static Log log = Log.getLog(SimpleQueryCompiler.class);

	/** {@inheritDoc} */
	@Override
	protected StringPart createDefaultAttributePart(String part) {
		ParameterPart out = new ParameterPart(this);
		out.attribute = MString.split(part,",");
		return out;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isParseAttributes() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public ParsingPart compileFunction(FunctionPart function) {
		return function;
	}

	/** {@inheritDoc} */
	@Override
	public String toSqlDateValue(Date string) {
		return "'" + MCast.toString(string) + "'";
	}

	/** {@inheritDoc} */
	@Override
	public String valueToString(Object value) {
		return MCast.objectToString(value);
	}

	/** {@inheritDoc} */
	@Override
	public String escape(String text) {
		return MSql.escape(text);
	}


}
