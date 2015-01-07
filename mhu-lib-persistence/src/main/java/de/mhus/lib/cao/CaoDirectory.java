package de.mhus.lib.cao;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.lang.MObject;

public class CaoDirectory extends MObject implements IBase {

	protected HashMap<String, CaoConnection> schemes = new HashMap<String, CaoConnection>();
	
	public CaoConnection getScheme(String name) {
		return schemes.get(name);
	}
	
	public InputStream getInputStream(URI uri) throws IOException {
		String schemeName = uri.getScheme();
		log().t(schemeName);
		CaoConnection scheme = getScheme(schemeName);
		if (scheme == null) return null;
		
		return scheme.getResource(uri.getPath()).getInputStream();
	}
	
	public InputStream getInputStream(String name) throws URISyntaxException, IOException {
		return getInputStream(new URI(name));
	}

	public URL getResource(URI uri) {
		String schemeName = uri.getScheme();
		log().t(schemeName);
		CaoConnection scheme = getScheme(schemeName);
		if (scheme == null) return null;
		return scheme.getResource(uri.getPath()).getUrl();
	}
	
	public URL getResource(String name) throws URISyntaxException {
		URI uri = new URI(name);
		return getResource(uri);
	}

}
