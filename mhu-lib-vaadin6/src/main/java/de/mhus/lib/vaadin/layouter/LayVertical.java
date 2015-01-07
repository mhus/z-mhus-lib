package de.mhus.lib.vaadin.layouter;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.errors.MException;

public class LayVertical extends DefComponent {

	public LayVertical(IDefDefinition... definitions)
			throws MException {
		super(LayoutBuilder.VERTICAL, definitions);
	}

}
