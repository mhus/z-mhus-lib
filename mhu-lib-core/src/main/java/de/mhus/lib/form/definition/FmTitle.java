package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;

/**
 * <p>FmTitle class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FmTitle implements IDefAttribute {

	private String title;
	private String descritpion;

	/**
	 * <p>Constructor for FmTitle.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public FmTitle(String title, String description) {
		this.title = title;
		this.descritpion = description;
	}
	
	/** {@inheritDoc} */
	@Override
	public void inject(DefComponent root) throws MException {
		if (title != null) root.setString("title", title);
		if (descritpion != null) root.setString("description", descritpion);
	}

}
