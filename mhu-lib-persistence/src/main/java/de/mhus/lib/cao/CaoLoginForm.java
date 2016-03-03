package de.mhus.lib.cao;

import java.net.URI;

import de.mhus.lib.core.form.MForm;

/**
 * <p>Abstract CaoLoginForm class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class CaoLoginForm extends MForm {

	/**
	 * <p>getURI.</p>
	 *
	 * @return a {@link java.net.URI} object.
	 */
	public abstract URI getURI();

	/**
	 * <p>getAuthentication.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public abstract String getAuthentication();

}
