package de.mhus.lib.errors;

/**
 * <p>SyntaxError class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class SyntaxError extends MException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1520109962430808111L;

	/**
	 * <p>Constructor for SyntaxError.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 */
	public SyntaxError(Object... in) {
		super(in);
	}

}
