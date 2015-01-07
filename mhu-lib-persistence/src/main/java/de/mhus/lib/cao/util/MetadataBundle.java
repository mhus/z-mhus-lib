package de.mhus.lib.cao.util;

import java.util.HashMap;
import java.util.Map;

import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoMetadata;

public class MetadataBundle {
	
	private CaoDriver driver;
	private Map<String, CaoMetadata> map = new HashMap<String,CaoMetadata>();

	public MetadataBundle(CaoDriver driver) {
		this.driver = driver;
	}
	
	public Map<String, CaoMetadata> getBundle() {
		return map;
	}

	public CaoDriver getDriver() {
		return driver;
	}
	
}
