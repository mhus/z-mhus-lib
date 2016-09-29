package de.mhus.lib.errors;

/**
 * <p>ValidationException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ValidationException extends MRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1520109962430808111L;

	/**
	 * <p>Constructor for ValidationException.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 */
	public ValidationException(Object... in) {
		super(in);
	}

	
}
