package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.IDefDefinition;

/**
 * <p>FmLayoutTabs class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FmLayoutTabs extends FmElement {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for FmLayoutTabs.</p>
	 *
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefDefinition} object.
	 */
	public FmLayoutTabs(IDefDefinition ... definitions) {
		this("","","", definitions);
	}
	/**
	 * <p>Constructor for FmLayoutTabs.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefDefinition} object.
	 */
	public FmLayoutTabs(String name, String title, String description, IDefDefinition ... definitions) {
		super(name, new FmNls(title, description));
		addDefinition(new DefAttribute("layout", "tabs"));
		setString("type", "tabs");
		addDefinition(definitions);
	}

}
