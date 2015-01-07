package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.errors.MException;

public class FmSplit extends DefComponent {

	public FmSplit(IDefDefinition[] definitions) throws MException {
		super("split", definitions);
	}

}
