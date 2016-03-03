package de.mhus.lib.form.control;

import de.mhus.lib.form.FormAction;
import de.mhus.lib.form.FormControl;

/**
 * <p>Action interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Action {

	/**
	 * <p>doExecute.</p>
	 *
	 * @param control a {@link de.mhus.lib.form.FormControl} object.
	 * @param action a {@link de.mhus.lib.form.FormAction} object.
	 */
	void doExecute(FormControl control, FormAction action);

}
