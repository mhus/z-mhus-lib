package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

/**
 * <p>FmPassword class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FmPassword extends FmElement {

	/**
	 * <p>Constructor for FmPassword.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public FmPassword(String name, String title, String description) {
		this(name, new FmNls(title, description));
	}

	/**
	 * <p>Constructor for FmPassword.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefAttribute} object.
	 */
	public FmPassword(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "password");
	}


}
