package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.errors.MException;

public class FmComponent extends DefComponent {

	public FmComponent(IDefDefinition ... definitions) throws MException {
		super("component", definitions);
	}

}
