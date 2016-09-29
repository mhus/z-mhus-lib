package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;

/**
 * <p>FmLink class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FmLink extends FmElement {

	/**
	 * <p>Constructor for FmLink.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param label a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefAttribute} object.
	 */
	public FmLink(String name, String label, String title, String description, IDefAttribute ... definitions) {
		this(name, new FmNls(title, description));
		setString("label", label);
		addDefinition(definitions);
	}
	
	/**
	 * <p>Constructor for FmLink.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefAttribute} object.
	 */
	public FmLink(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "link");
	}


}
