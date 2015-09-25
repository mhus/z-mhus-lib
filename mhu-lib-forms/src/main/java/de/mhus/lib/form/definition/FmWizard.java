package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.control.Wizard;

public class FmWizard extends DefAttribute {

	private DefAttribute[] options;

	public FmWizard(String handler, DefAttribute ... options) {
		super("wizard", handler);
		this.options = options;
	}
	
	public FmWizard(Class<? extends Wizard> handler, DefAttribute ... options) {
		super("wizard", handler.getCanonicalName());
		this.options = options;
	}

	@Override
	public void inject(DefComponent parent) throws MException {
		super.inject(parent);
		if (options != null) {
			DefComponent dummy = new DefComponent("wizard", options);
			parent.setConfig("wizard", dummy);
			dummy.inject(null);
		}
	}
	
}
