package de.mhus.lib.core.lang;

import de.mhus.lib.errors.MException;

public class AlreadyBoundException extends MException {

	private static final long serialVersionUID = 1L;

	public AlreadyBoundException(Object... in) {
		super(in);
	}

}
