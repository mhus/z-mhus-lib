package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

/**
 * <p>FmPassword class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmPassword extends FmElement {

	/**
	 * <p>Constructor for FmPassword.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 * @since 3.2.9
	 */
	public FmPassword(String name, String title, String description) {
		this(name, new FmNls(title, description), new FmDefaultSources());
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
