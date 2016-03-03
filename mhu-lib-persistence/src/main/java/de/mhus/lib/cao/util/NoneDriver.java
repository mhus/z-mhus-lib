package de.mhus.lib.cao.util;

import java.net.URI;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoLoginForm;

/**
 * <p>NoneDriver class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class NoneDriver extends CaoDriver {

	private static NoneDriver instance;

	/** {@inheritDoc} */
	@Override
	public CaoConnection connect(URI uri, String authentication) {
		return new NoneConnection(this);
	}

	/**
	 * <p>Getter for the field <code>instance</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public static synchronized CaoDriver getInstance() {
		if (instance == null)
			instance = new NoneDriver();
		return instance;
	}

	/** {@inheritDoc} */
	@Override
	public CaoLoginForm createLoginForm(URI uri, String authentication) {
		return null;
	}

}
