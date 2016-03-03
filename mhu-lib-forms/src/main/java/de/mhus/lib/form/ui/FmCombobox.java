package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

/**
 * <p>FmCombobox class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmCombobox extends FmElement {

	/** Constant <code>TYPE_COMBOBOX="combobox"</code> */
	public static final String TYPE_COMBOBOX = "combobox";

	/**
	 * <p>Constructor for FmCombobox.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 * @since 3.2.9
	 */
	public FmCombobox(String name, String title, String description) {
		this(name, new FmNls(title, description), new FmDefaultSources());
	}

	/**
	 * <p>Constructor for FmCombobox.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefAttribute} object.
	 */
	public FmCombobox(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString(FmElement.TYPE, TYPE_COMBOBOX);
	}


}
