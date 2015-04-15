package de.mhus.lib.vaadin.aqua;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.errors.MException;
import de.mhus.lib.vaadin.layouter.LayoutBuilder;

public class DskMainMenu extends DefComponent {

	public DskMainMenu(IDefDefinition ... definitions)
			throws MException {
		super("mainmenu", definitions);
		addDefinition(new DefAttribute(LayoutBuilder.EXPAND, "0"));
	}

}
