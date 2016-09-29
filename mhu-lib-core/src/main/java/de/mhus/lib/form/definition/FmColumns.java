package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;

/**
 * <p>FmColumns class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FmColumns extends DefAttribute {

	/**
	 * <p>Constructor for FmColumns.</p>
	 *
	 * @param cols a int.
	 */
	public FmColumns(int cols) {
		super("columns", String.valueOf(cols));
	}

}
