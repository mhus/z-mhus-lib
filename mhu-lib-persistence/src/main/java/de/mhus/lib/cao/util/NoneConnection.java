package de.mhus.lib.cao.util;

import de.mhus.lib.cao.CaoCore;
import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoNode;

/**
 * <p>NoneConnection class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class NoneConnection extends CaoCore {

	/**
	 * <p>Constructor for NoneConnection.</p>
	 *
	 * @param driver a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public NoneConnection(String name, CaoDriver driver) {
		super(name, driver);
		this.con = this;
	}

	/** {@inheritDoc} */
	@Override
	public CaoNode getResourceByPath(String name) {
		return null;
	}

	@Override
	public CaoNode getRoot() {
		return null;
	}

	@Override
	public CaoNode getResourceById(String id) {
		return null;
	}


}
