package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

/**
 * <p>FmDate class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmDate extends FmElement {

	public enum FORMATS {DATE,DATETIME,DATETIMESECONDS,TIME,TIMESECONDS};
	
	/** Constant <code>FORMAT="format"</code> */
	public static final String FORMAT = "format";

	/**
	 * <p>Constructor for FmDate.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param format a {@link de.mhus.lib.form.ui.FmDate.FORMATS} object.
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public FmDate(String name, FORMATS format, String title, String description) {
		this(name, new DefAttribute(FORMAT, format.name()), new FmNls(title, description), new FmDefaultSources());
	}
//	public FmDate(String name, String title, String description) {
//		this(name, new FmNls(title, description), new FmDefaultSources());
//	}

	/**
	 * <p>Constructor for FmDate.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefAttribute} object.
	 */
	public FmDate(String name, IDefAttribute ... definitions) {
		super(name, definitions);
		setString(FmElement.TYPE, FORMATS.DATE.name());
	}


}
