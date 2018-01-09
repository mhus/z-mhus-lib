package de.mhus.lib.core.config;

import java.io.File;
import java.io.FileNotFoundException;

public class DefaultConfigFile extends XmlConfigFile {

	private static final long serialVersionUID = 1L;

	public DefaultConfigFile() throws FileNotFoundException, Exception {
		super(new File("config.xml"));
	}

}
