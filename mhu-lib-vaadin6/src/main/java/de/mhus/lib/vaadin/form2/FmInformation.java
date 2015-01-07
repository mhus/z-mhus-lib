package de.mhus.lib.vaadin.form2;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.definition.FmElement;

public class FmInformation extends FmElement {

	public FmInformation(IDefAttribute ... definitions) throws MException {
		super(DataSource.NAME_INFORMATION, definitions);
		setString("type", "information");
		setString("fullwidth", "true");
		setString("titleinside", "true");
	}


}
