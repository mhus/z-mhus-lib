package de.mhus.lib.cao;

import de.mhus.lib.core.directory.MResourceProvider;

/**
 * <p>Abstract CaoConnection class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class CaoConnection extends MResourceProvider<CaoNode> {

	protected CaoDriver driver;

	/**
	 * <p>Constructor for CaoConnection.</p>
	 *
	 * @param driver a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public CaoConnection(CaoDriver driver) {
		this.driver = driver;
	}

	/**
	 * <p>Getter for the field <code>driver</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public CaoDriver getDriver() {
		return driver;
	}

}
