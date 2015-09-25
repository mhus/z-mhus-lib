package de.mhus.lib.form.control;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataConnector;
import de.mhus.lib.form.FormAction;
import de.mhus.lib.form.FormControl;
import de.mhus.lib.form.LayoutElement;
import de.mhus.lib.form.validation.Validator;

public class ActivatorControl implements FormControl {

	private FocusManager focusManager;
//	private MActivator wizards;
//	private MActivator validators;
//	private MActivator actions;
	
	@Override
	public void focused(LayoutElement element) {
		if (focusManager != null)
			focusManager.focused(element);
	}

	@Override
	public void action(FormAction action) {
//		MActivator a = wizards;
//		if (a == null) a = action.getParent().getLayoutFactory().getActivator();
		MActivator a = action.getParent().getLayoutFactory().getActivator();
		Action act;
		try {
//			act = (Action) a.getObject(Action.class,action.getConfig().getString("action", null));
			act = (Action) a.getObject(action.getConfig().getString("action", null));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (act != null)
			act.doExecute(this,action);
	}

	@Override
	public void wizard(LayoutElement element) {
//		MActivator a = wizards;
//		if (a == null) a = element.getLayoutFactory().getActivator();
		MActivator a = element.getLayoutFactory().getActivator();
		Wizard wizard;
		try {
//			wizard = (Wizard) a.getObject(Wizard.class,element.getConfig().getString("wizard", null));
			wizard = (Wizard) a.getObject(element.getConfig().getString("wizard", null));
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (wizard != null)
			wizard.openWizard(this,element);
	}

	@Override
	public boolean validate(LayoutElement element, DataConnector dataConnector,
			Object value) throws MException {
		
		String string = element.getConfig().getString("validator", null);
		if (string == null) return true;
		
//		MActivator a = validators;
//		if (a == null) a = element.getLayoutFactory().getActivator();
		MActivator a = element.getLayoutFactory().getActivator();
		
		String[] va = string.split(",");
		for (String v : va) {
			Validator validator;
			try {
				validator = (Validator) a.getObject(Validator.class,v);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			if (validator != null && !validator.validate(this,element,value))
				return false;
		}
		return true;
	}

	public FocusManager getFocusManager() {
		return focusManager;
	}

	public void setFocusManager(FocusManager focusManager) {
		this.focusManager = focusManager;
	}

}
