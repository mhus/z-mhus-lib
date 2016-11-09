package de.mhus.lib.cao;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import de.mhus.lib.core.lang.MObject;

/**
 * <p>Abstract CaoDriver class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class CaoDriver extends MObject {

	protected String scheme;

	/**
	 * <p>connect.</p>
	 *
	 * @param uri a {@link java.lang.String} object.
	 * @param authentication a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.cao.CaoConnection} object.
	 * @throws java.net.URISyntaxException if any.
	 */
	public CaoConnection connect(String uri, String authentication) throws URISyntaxException {
		if (uri == null)
			return connect((URI)null, authentication);
		if (uri.indexOf(':') < 0)
			return connect(new File(uri).toURI(), authentication);
		return connect(new URI(uri), authentication);
	}

	/**
	 * <p>connect.</p>
	 *
	 * @param uri a {@link java.net.URI} object.
	 * @param authentication a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.cao.CaoConnection} object.
	 */
	public abstract CaoConnection connect(URI uri, String authentication);

	/**
	 * <p>Getter for the field <code>scheme</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * <p>createLoginForm.</p>
	 *
	 * @param uri a {@link java.net.URI} object.
	 * @param authentication a {@link java.lang.String} object.
	 * @return a {@link de.mhus.lib.cao.CaoLoginForm} object.
	 */
	public abstract CaoLoginForm createLoginForm(URI uri, String authentication);

}
