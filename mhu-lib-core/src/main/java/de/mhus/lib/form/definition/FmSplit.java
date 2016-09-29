package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;

/**
 * <p>FmSplit class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FmSplit extends DefComponent {

	/**
	 * <p>Constructor for FmSplit.</p>
	 *
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefDefinition} object.
	 */
	public FmSplit(IDefDefinition ... definitions) {
		super("split", definitions);
	}

}
