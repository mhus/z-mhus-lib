package de.mhus.lib.cao;

/**
 * <p>CaoUser class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class CaoUser extends CaoPrincipal {

	/**
	 * <p>Constructor for CaoUser.</p>
	 *
	 * @param element a {@link de.mhus.lib.cao.CaoNode} object.
	 * @param name a {@link java.lang.String} object.
	 * @param readable a boolean.
	 * @param writable a boolean.
	 * @throws de.mhus.lib.cao.CaoException if any.
	 */
	public CaoUser(CaoNode element, String name, boolean readable,
			boolean writable) throws CaoException {
		super(element, name, PRINCIPAL_TYPES.USER, readable, writable);
	}

}
