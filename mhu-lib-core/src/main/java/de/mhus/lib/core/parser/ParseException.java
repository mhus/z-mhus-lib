package de.mhus.lib.core.parser;

import de.mhus.lib.errors.MException;

public class ParseException extends MException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8355846724520161688L;

	public ParseException(Object... in) {
		super(in);
	}

}
