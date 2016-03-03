package de.mhus.lib.core.parser;

import de.mhus.lib.errors.MException;

/**
 * <p>ParseException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ParseException extends MException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8355846724520161688L;

	/**
	 * <p>Constructor for ParseException.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 */
	public ParseException(Object... in) {
		super(in);
	}

}
