package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;

/**
 * <p>FmOverlay class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class FmOverlay extends DefComponent {

	/**
	 * <p>Constructor for FmOverlay.</p>
	 *
	 * @param definitions an array of {@link de.mhus.lib.core.definition.IDefDefinition} objects.
	 */
	public FmOverlay(IDefDefinition[] definitions) {
		super("overlay", definitions);
	}

}
