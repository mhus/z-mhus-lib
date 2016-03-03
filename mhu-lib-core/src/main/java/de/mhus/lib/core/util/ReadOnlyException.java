package de.mhus.lib.core.util;

import de.mhus.lib.errors.MException;

/**
 * <p>ReadOnlyException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ReadOnlyException extends MException {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for ReadOnlyException.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 */
	public ReadOnlyException(Object... in) {
		super(in);
	}

}
