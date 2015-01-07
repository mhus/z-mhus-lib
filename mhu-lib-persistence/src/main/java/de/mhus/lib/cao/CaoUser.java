package de.mhus.lib.cao;

public class CaoUser extends CaoPrincipal {

	public CaoUser(CaoNode element, String name, boolean readable,
			boolean writable) throws CaoException {
		super(element, name, PRINCIPAL_TYPES.USER, readable, writable);
	}
	
}
