package de.mhus.lib.vaadin.aqua;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.errors.MException;

public class DskLogin extends DefComponent {

	public DskLogin(IDefDefinition ... definitions)
			throws MException {
		super("login", definitions);
		//addDefinition(new DefAttribute(LayoutBuilder.EXPAND, "0"));
	}

}
