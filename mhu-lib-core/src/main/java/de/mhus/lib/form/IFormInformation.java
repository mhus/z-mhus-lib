package de.mhus.lib.form;

import de.mhus.lib.core.definition.DefRoot;

public interface IFormInformation {

	/**
	 * Get form definition.
	 * @return form definition
	 */
	DefRoot getForm();

	/**
	 * Get Action Handler Class or null
	 * @return Action handler
	 */
	Class<? extends ActionHandler> getActionHandler();
	
	/**
	 * Get Form Control Class or null
	 * @return Form Control
	 */
	Class<? extends FormControl> getFormControl();
	
}
