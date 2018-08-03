package de.mhus.lib.form;

import de.mhus.lib.core.definition.DefRoot;

public class DefaultFormInformation implements IFormInformation {

	private DefRoot form;
	private Class<? extends ActionHandler> actionHandler;
	private Class<? extends FormControl> formControl;

	
	public DefaultFormInformation(DefRoot form, Class<? extends ActionHandler> actionHandler,
	        Class<? extends FormControl> formControl) {
		this.form = form;
		this.actionHandler = actionHandler;
		this.formControl = formControl;
	}

	@Override
	public DefRoot getForm() {
		return form;
	}

	@Override
	public Class<? extends ActionHandler> getActionHandler() {
		return actionHandler;
	}

	@Override
	public Class<? extends FormControl> getFormControl() {
		return formControl;
	}

}
