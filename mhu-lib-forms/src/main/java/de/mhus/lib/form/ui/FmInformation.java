package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.definition.FmElement;

public class FmInformation extends FmElement {

	public FmInformation(IDefAttribute ... definitions) {
		super(DataSource.NAME_INFORMATION, definitions);
		setString("type", "information");
		setString("fullwidth", "true");
		setString("titleinside", "true");
	}


}
