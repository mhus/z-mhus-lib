package de.mhus.lib.core.config;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * <p>DefaultConfigFile class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefaultConfigFile extends XmlConfigFile {

	/**
	 * <p>Constructor for DefaultConfigFile.</p>
	 *
	 * @throws java.io.FileNotFoundException if any.
	 * @throws java.lang.Exception if any.
	 */
	public DefaultConfigFile() throws FileNotFoundException, Exception {
		super(new File("config.xml"));
	}

}
