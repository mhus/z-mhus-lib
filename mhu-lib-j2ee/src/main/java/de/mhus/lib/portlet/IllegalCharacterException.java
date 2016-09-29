package de.mhus.lib.portlet;

/**
 * <p>IllegalCharacterException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class IllegalCharacterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for IllegalCharacterException.</p>
	 *
	 * @param c a char.
	 * @param name a {@link java.lang.String} object.
	 */
	public IllegalCharacterException(char c, String name) {
		super("illegal " + c + " character in name " + name);
	}

	
}
