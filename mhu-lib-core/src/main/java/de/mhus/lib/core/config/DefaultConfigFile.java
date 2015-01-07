package de.mhus.lib.core.config;

import java.io.File;
import java.io.FileNotFoundException;

public class DefaultConfigFile extends XmlConfigFile {

	public DefaultConfigFile() throws FileNotFoundException, Exception {
		super(new File("config.xml"));
	}

}
