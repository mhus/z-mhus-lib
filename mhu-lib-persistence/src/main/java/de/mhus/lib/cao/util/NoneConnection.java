package de.mhus.lib.cao.util;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoNode;

/**
 * <p>NoneConnection class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class NoneConnection extends CaoConnection {

	/**
	 * <p>Constructor for NoneConnection.</p>
	 *
	 * @param driver a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public NoneConnection(CaoDriver driver) {
		super(driver);
	}

	/** {@inheritDoc} */
	@Override
	public CaoNode getResource(String name) {
		return null;
	}

	@Override
	public CaoNode getRoot() {
		return null;
	}


}
