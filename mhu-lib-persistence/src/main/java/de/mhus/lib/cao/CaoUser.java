package de.mhus.lib.cao;

import de.mhus.lib.errors.MException;

public class CaoUser extends CaoPrincipal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CaoUser(CaoCore core, CaoNode element, String name, boolean readable,
			boolean writable) throws MException {
		super(core, element, name, PRINCIPAL_TYPES.USER, readable, writable);
	}

}
