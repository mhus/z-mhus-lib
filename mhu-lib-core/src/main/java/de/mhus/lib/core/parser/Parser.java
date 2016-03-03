package de.mhus.lib.core.parser;

/**
 * <p>Parser interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Parser {

	/**
	 * <p>compileString.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.core.parser.CompiledString} object.
	 * @throws de.mhus.lib.core.parser.ParseException if any.
	 */
	public CompiledString compileString(String in) throws ParseException;

}
