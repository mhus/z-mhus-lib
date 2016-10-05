package de.mhus.lib.core.util;

import de.mhus.lib.errors.MException;

public class ReadOnlyException extends MException {

	private static final long serialVersionUID = 1L;

	public ReadOnlyException(Object... in) {
		super(in);
	}

}
