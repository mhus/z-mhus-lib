package de.mhus.lib.form.definition;

import java.util.UUID;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.errors.MException;

/**
 * <p>FmNls class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmNls extends DefAttribute {

	private String title;
	private String descritpion;

	/**
	 * <p>Constructor for FmNls.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public FmNls(String value) {
		this(value,null,null);
	}
	
	/**
	 * <p>Constructor for FmNls.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public FmNls(String title, String description) {
		this(null,title,description);
	}
	
	/**
	 * <p>Constructor for FmNls.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public FmNls(String value, String title, String description) {
		super("nls", value == null ? UUID.randomUUID().toString() : value);
		this.title = title;
		this.descritpion = description;

	}

	/** {@inheritDoc} */
	@Override
	public void inject(DefComponent root) throws MException {
		super.inject(root);
		if (title != null) root.setString("title", title);
		if (descritpion != null) root.setString("description", descritpion);
	}

}
