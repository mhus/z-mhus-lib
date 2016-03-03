package de.mhus.lib.form.control;

import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;

/**
 * <p>Wizard interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public interface Wizard {

	/**
	 * <p>openWizard.</p>
	 *
	 * @param control a {@link de.mhus.lib.form.FormControl} object.
	 * @param element a {@link de.mhus.lib.form.LayoutElement} object.
	 */
	void openWizard(FormControl control, LayoutElement element);

}
