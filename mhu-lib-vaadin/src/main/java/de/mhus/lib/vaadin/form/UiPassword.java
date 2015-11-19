package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.Form;

public class UiPassword extends UiVaadin {

	public UiPassword(Form form, IConfig config) {
		super(form, config);
	}

	@Override
	protected void setValue(Object value) throws MException {
		((PasswordField)getComponentError()).setValue(MCast.toString(value));
	}

	@Override
	public Component createEditor() {
		return new PasswordField();
	}

	@Override
	protected Object getValue() throws MException {
		return ((PasswordField)getComponentEditor()).getValue();
	}

}
