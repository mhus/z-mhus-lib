package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

/**
 * <p>FmText class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmText extends FmElement {

	/**
	 * <p>Constructor for FmText.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 * @since 3.2.9
	 */
	public FmText(String name, String title, String description) {
		this(name, new FmNls(title, description), new FmDefaultSources());
	}
	
	/**
	 * <p>Constructor for FmText.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefAttribute} object.
	 */
	public FmText(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type", "text");
	}


}
