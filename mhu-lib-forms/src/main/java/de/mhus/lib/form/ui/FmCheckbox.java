package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

public class FmCheckbox extends FmElement {

	public FmCheckbox(String name, String title, String description) {
		this(name, new FmNls(title, description), new FmDefaultSources());
	}

	public FmCheckbox(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type","checkbox");
	}
	
	public FmCheckbox defaultValue(boolean in) throws MException {
		setBoolean("default", in);
		return this;
	}
	
}
