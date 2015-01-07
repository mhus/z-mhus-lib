package de.mhus.lib.form.control;

import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;

public abstract class AbstractWizzard implements Wizzard {

	@Override
	public void openWizzard(FormControl control, LayoutElement element) {
		WizzardCall call = new WizzardCall(control, element);
		doExecute(call);
	}

	protected abstract void doExecute(WizzardCall call);
	
}
