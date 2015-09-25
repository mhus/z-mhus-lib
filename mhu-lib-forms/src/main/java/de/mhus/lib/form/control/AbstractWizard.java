package de.mhus.lib.form.control;

import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;

public abstract class AbstractWizard implements Wizard {

	@Override
	public void openWizard(FormControl control, LayoutElement element) {
		WizardCall call = new WizardCall(control, element);
		doExecute(call);
	}

	protected abstract void doExecute(WizardCall call);
	
}
