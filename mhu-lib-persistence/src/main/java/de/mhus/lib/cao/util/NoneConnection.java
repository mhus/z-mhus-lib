package de.mhus.lib.cao.util;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoNode;

public class NoneConnection extends CaoConnection {

	public NoneConnection(CaoDriver driver) {
		super(driver);
	}

	@Override
	public CaoNode getResource(String name) {
		return null;
	}


}
