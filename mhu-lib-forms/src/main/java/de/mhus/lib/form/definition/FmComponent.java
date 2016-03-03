package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;

/**
 * <p>FmComponent class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmComponent extends DefComponent {

	/**
	 * <p>Constructor for FmComponent.</p>
	 *
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefDefinition} object.
	 */
	public FmComponent(IDefDefinition ... definitions) {
		super("component", definitions);
	}

}
