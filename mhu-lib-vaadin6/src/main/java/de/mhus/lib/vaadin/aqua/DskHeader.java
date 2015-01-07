package de.mhus.lib.vaadin.aqua;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.errors.MException;
import de.mhus.lib.vaadin.layouter.LayoutBuilder;

public class DskHeader extends DefComponent {

	public DskHeader(IDefDefinition ... definitions)
			throws MException {
		super("header", definitions);
		addDefinition(new DefAttribute(LayoutBuilder.EXPAND, "0"));
	}

}
