package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.form.DataSource;
import de.mhus.lib.form.definition.FmElement;

/**
 * <p>FmInformation class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmInformation extends FmElement {

	/**
	 * <p>Constructor for FmInformation.</p>
	 *
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefAttribute} object.
	 */
	public FmInformation(IDefAttribute ... definitions) {
		super(DataSource.NAME_INFORMATION, definitions);
		setString(FmElement.TYPE, "information");
		setString(FmElement.FULLWIDTH, FmElement.TRUE);
		setString(FmElement.TITLEINSIDE, FmElement.TRUE);
	}


}
