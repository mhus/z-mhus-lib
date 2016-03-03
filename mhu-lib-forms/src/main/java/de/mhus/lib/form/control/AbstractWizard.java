package de.mhus.lib.form.control;

import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;

/**
 * <p>Abstract AbstractWizard class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public abstract class AbstractWizard implements Wizard {

	/** {@inheritDoc} */
	@Override
	public void openWizard(FormControl control, LayoutElement element) {
		WizardCall call = new WizardCall(control, element);
		doExecute(call);
	}

	/**
	 * <p>doExecute.</p>
	 *
	 * @param call a {@link de.mhus.lib.form.control.WizardCall} object.
	 */
	protected abstract void doExecute(WizardCall call);
	
}
