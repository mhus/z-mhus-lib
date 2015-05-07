package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.DefAttribute;
import de.mhus.lib.core.definition.DefComponent;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.control.Wizzard;

public class FmWizzard extends DefAttribute {

	private DefAttribute[] options;

	public FmWizzard(Class<? extends Wizzard> handler, DefAttribute ... options) {
		super("wizzard", handler.getCanonicalName());
		this.options = options;
	}

	@Override
	public void inject(DefComponent parent) throws MException {
		super.inject(parent);
		if (options != null) {
			DefComponent dummy = new DefComponent("wizzard", options);
			parent.setConfig("wizzard", dummy);
			dummy.inject(null);
		}
	}
	
}
