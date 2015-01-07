package de.mhus.lib.cao;

import java.net.URI;
import java.net.URISyntaxException;

import de.mhus.lib.core.lang.MObject;

public abstract class CaoDriver extends MObject {

	protected String scheme;

	public CaoConnection connect(String uri, String authentication) throws URISyntaxException {
		return connect(new URI(uri), authentication);
	}
	
	public abstract CaoConnection connect(URI uri, String authentication);

	public String getScheme() {
		return scheme;
	}

	public abstract CaoLoginForm createLoginForm(URI uri, String authentication);
	
}
