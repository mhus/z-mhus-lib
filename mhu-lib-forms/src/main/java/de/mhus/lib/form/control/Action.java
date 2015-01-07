package de.mhus.lib.form.control;

import de.mhus.lib.form.FormAction;
import de.mhus.lib.form.FormControl;

public interface Action {

	void doExecute(FormControl control, FormAction action);

}
