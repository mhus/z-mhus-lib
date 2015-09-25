package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

public class FmCombobox extends FmElement {

	public static final String TYPE_COMBOBOX = "combobox";

	public FmCombobox(String name, String title, String description) {
		this(name, new FmNls(title, description), new FmDefaultSources());
	}

	public FmCombobox(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString(FmElement.TYPE, TYPE_COMBOBOX);
	}


}
