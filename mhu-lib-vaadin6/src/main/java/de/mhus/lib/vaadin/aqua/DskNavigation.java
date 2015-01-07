package de.mhus.lib.vaadin.aqua;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.errors.MException;

public class DskNavigation extends DefComponent {

	public DskNavigation(IDefDefinition ... definitions)
			throws MException {
		super("navigation", definitions);
//		addDefinition(new DefAttribute(LayoutBuilder.EXPAND, "0"));
	}

}
