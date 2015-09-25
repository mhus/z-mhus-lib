package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

public class FmDate extends FmElement {

	public enum FORMATS {DATE,DATETIME,DATETIMESECONDS,TIME,TIMESECONDS};
	
	public static final String FORMAT = "format";

	public FmDate(String name, FORMATS format, String title, String description) {
		this(name, new DefAttribute(FORMAT, format.name()), new FmNls(title, description), new FmDefaultSources());
	}
//	public FmDate(String name, String title, String description) {
//		this(name, new FmNls(title, description), new FmDefaultSources());
//	}

	public FmDate(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString(FmElement.TYPE, FORMATS.DATE.name());
	}


}
