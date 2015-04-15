package de.mhus.lib.cao;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.lang.MObject;

@DefaultImplementation(DriverManagerImpl.class)
public class CaoDriverManager extends MObject implements IBase {

	protected HashMap<String, CaoDriver> schemes = new HashMap<String, CaoDriver>();

	public CaoDriver getScheme(String name) {
		return schemes.get(name);
	}

	public CaoConnection connect(String uri, String authentication) throws URISyntaxException {
		return connect(new URI(uri), authentication);
	}

	public CaoConnection connect(URI uri, String authentication) {
		log().t("connect",uri);
		CaoDriver scheme = getScheme(uri.getScheme());
		if (scheme == null) return null;
		return scheme.connect(uri, authentication);
	}


}
