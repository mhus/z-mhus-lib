package de.mhus.lib.sql.parser;

import java.util.Date;

import de.mhus.lib.core.parser.ParsingPart;

/**
 * <p>ICompiler interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface ICompiler {

	/**
	 * <p>isParseAttributes.</p>
	 *
	 * @return a boolean.
	 */
	boolean isParseAttributes();

	/**
	 * <p>compileFunction.</p>
	 *
	 * @param function a {@link de.mhus.lib.sql.parser.FunctionPart} object.
	 * @return a {@link de.mhus.lib.core.parser.ParsingPart} object.
	 */
	ParsingPart compileFunction(FunctionPart function);

	/**
	 * <p>toSqlDateValue.</p>
	 *
	 * @param string a {@link java.util.Date} object.
	 * @return a {@link java.lang.String} object.
	 */
	String toSqlDateValue(Date string);

	/**
	 * <p>valueToString.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 */
	String valueToString(Object value);

	/**
	 * <p>escape.</p>
	 *
	 * @param text a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String escape(String text);

}
