package de.mhus.lib.cao.util;

import java.net.URI;

import de.mhus.lib.cao.CaoConnection;
import de.mhus.lib.cao.CaoCore;
import de.mhus.lib.cao.CaoDriver;
import de.mhus.lib.cao.CaoLoginForm;

public class NoneDriver extends CaoDriver {

	private static NoneDriver instance;

	@Override
	public CaoCore connect(URI uri, String authentication) {
		return new NoneConnection("none",this);
	}

	public static synchronized CaoDriver getInstance() {
		if (instance == null)
			instance = new NoneDriver();
		return instance;
	}

	@Override
	public CaoLoginForm createLoginForm(URI uri, String authentication) {
		return null;
	}

}
