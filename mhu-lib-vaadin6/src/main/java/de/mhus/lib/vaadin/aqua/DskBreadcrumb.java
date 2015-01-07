package de.mhus.lib.vaadin.aqua;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.core.definition.IDefDefinition;
import de.mhus.lib.errors.MException;
import de.mhus.lib.vaadin.layouter.LayoutBuilder;

public class DskBreadcrumb extends DefComponent {

	public DskBreadcrumb(IDefDefinition ... definitions)
			throws MException {
		super("breadcrumb", definitions);
		addDefinition(new DefAttribute(LayoutBuilder.EXPAND, "0"));
	}

}
