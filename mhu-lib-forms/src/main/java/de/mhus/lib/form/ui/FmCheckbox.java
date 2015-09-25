package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

public class FmCheckbox extends FmElement {

	public static final String TYPE_CHECKBOX = "checkbox";

	public FmCheckbox(String name, String title, String description) {
		this(name, new FmNls(title, description), new FmDefaultSources());
	}

	public FmCheckbox(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString(FmElement.TYPE,TYPE_CHECKBOX);
	}
	
	public FmCheckbox defaultValue(boolean in) throws MException {
		setBoolean(FmElement.DEFAULT, in);
		return this;
	}
	
}
