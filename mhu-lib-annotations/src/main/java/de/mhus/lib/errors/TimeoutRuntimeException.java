package de.mhus.lib.errors;

/**
 * <p>TimeoutRuntimeException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class TimeoutRuntimeException extends MRuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for TimeoutRuntimeException.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 */
	public TimeoutRuntimeException(Object... in) {
		super(in);
	}

	
	
}
