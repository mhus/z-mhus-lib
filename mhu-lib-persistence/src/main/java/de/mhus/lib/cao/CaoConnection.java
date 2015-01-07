package de.mhus.lib.cao;

import de.mhus.lib.core.directory.MResourceProvider;

public abstract class CaoConnection extends MResourceProvider<CaoNode> {

	protected CaoDriver driver;
	
	public CaoConnection(CaoDriver driver) {
		this.driver = driver;
	}
	
	public CaoDriver getDriver() {
		return driver;
	}
		
}
