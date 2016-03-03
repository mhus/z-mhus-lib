package de.mhus.lib.cao.util;

import java.util.HashMap;
import java.util.Map;

import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoMetadata;

/**
 * <p>MetadataBundle class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MetadataBundle {

	private CaoDriver driver;
	private Map<String, CaoMetadata> map = new HashMap<String,CaoMetadata>();

	/**
	 * <p>Constructor for MetadataBundle.</p>
	 *
	 * @param driver a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public MetadataBundle(CaoDriver driver) {
		this.driver = driver;
	}

	/**
	 * <p>getBundle.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, CaoMetadata> getBundle() {
		return map;
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
