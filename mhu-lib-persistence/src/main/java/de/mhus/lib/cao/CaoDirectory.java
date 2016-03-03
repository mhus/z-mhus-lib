package de.mhus.lib.cao;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>CaoDirectory class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class CaoDirectory extends MObject implements IBase {

	protected HashMap<String, CaoConnection> schemes = new HashMap<String, CaoConnection>();

	/**
	 * <p>getScheme.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.cao.CaoConnection} object.
	 */
	public CaoConnection getScheme(String name) {
		return schemes.get(name);
	}

	/**
	 * <p>getInputStream.</p>
	 *
	 * @param uri a {@link java.net.URI} object.
	 * @return a {@link java.io.InputStream} object.
	 * @throws java.io.IOException if any.
	 */
	public InputStream getInputStream(URI uri) throws IOException {
		String schemeName = uri.getScheme();
		log().t(schemeName);
		CaoConnection scheme = getScheme(schemeName);
		if (scheme == null) return null;

		return scheme.getResource(uri.getPath()).getInputStream();
	}

	/**
	 * <p>getInputStream.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.io.InputStream} object.
	 * @throws java.net.URISyntaxException if any.
	 * @throws java.io.IOException if any.
	 */
	public InputStream getInputStream(String name) throws URISyntaxException, IOException {
		return getInputStream(new URI(name));
	}

	/**
	 * <p>getResource.</p>
	 *
	 * @param uri a {@link java.net.URI} object.
	 * @return a {@link java.net.URL} object.
	 */
	public URL getResource(URI uri) {
		String schemeName = uri.getScheme();
		log().t(schemeName);
		CaoConnection scheme = getScheme(schemeName);
		if (scheme == null) return null;
		return scheme.getResource(uri.getPath()).getUrl();
	}

	/**
	 * <p>getResource.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.net.URL} object.
	 * @throws java.net.URISyntaxException if any.
	 */
	public URL getResource(String name) throws URISyntaxException {
		URI uri = new URI(name);
		return getResource(uri);
	}

}
