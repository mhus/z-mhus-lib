package de.mhus.lib.vaadin.aqua;

import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.errors.MException;

public class DskContent extends DefComponent {

	public DskContent(IDefDefinition ... definitions)
			throws MException {
		super("content", definitions);
//		addDefinition(new DefAttribute(LayoutBuilder.EXPAND, "1"));
	}

}
