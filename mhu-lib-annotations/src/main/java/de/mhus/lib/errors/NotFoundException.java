package de.mhus.lib.errors;

/**
 * <p>NotFoundException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class NotFoundException extends MRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1520109962430808111L;

	/**
	 * <p>Constructor for NotFoundException.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 */
	public NotFoundException(Object... in) {
		super(in);
	}

	
}
