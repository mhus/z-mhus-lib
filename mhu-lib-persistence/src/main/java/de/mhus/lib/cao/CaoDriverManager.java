package de.mhus.lib.cao;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>CaoDriverManager class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
@DefaultImplementation(DriverManagerImpl.class)
public class CaoDriverManager extends MObject implements IBase {

	protected HashMap<String, CaoDriver> schemes = new HashMap<String, CaoDriver>();

	/**
	 * <p>getScheme.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.cao.CaoDriver} object.
	 */
	public CaoDriver getScheme(String name) {
		return schemes.get(name);
	}

	/**
	 * <p>connect.</p>
	 *
	 * @param uri a {@link java.lang.String} object.
	 * @param authentication a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.cao.CaoConnection} object.
	 * @throws java.net.URISyntaxException if any.
	 */
	public CaoConnection connect(String uri, String authentication) throws URISyntaxException {
		return connect(new URI(uri), authentication);
	}

	/**
	 * <p>connect.</p>
	 *
	 * @param uri a {@link java.net.URI} object.
	 * @param authentication a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.cao.CaoConnection} object.
	 */
	public CaoConnection connect(URI uri, String authentication) {
		log().t("connect",uri);
		CaoDriver scheme = getScheme(uri.getScheme());
		if (scheme == null) return null;
		return scheme.connect(uri, authentication);
	}


}
