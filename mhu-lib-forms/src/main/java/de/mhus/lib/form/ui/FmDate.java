package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

public class FmDate extends FmElement {

	public static final String FORMAT = "format";
	public static final String FORMAT_DATE = "date";
	public static final String FORMAT_DATETIME = "datetime";
	public static final String FORMAT_DATETIMESECONDS = "datetimesec";
	public static final String TYPE_DATE = "date";

	public FmDate(String name, String title, String description) {
		this(name, new FmNls(title, description), new FmDefaultSources());
	}

	public FmDate(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString(FmElement.TYPE, TYPE_DATE);
	}


}
