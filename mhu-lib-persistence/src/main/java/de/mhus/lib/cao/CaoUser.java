package de.mhus.lib.cao;

import de.mhus.lib.errors.MException;

public class CaoUser extends CaoPrincipal {

	public CaoUser(CaoNode element, String name, boolean readable,
			boolean writable) throws MException {
		super(element, name, PRINCIPAL_TYPES.USER, readable, writable);
	}

}
