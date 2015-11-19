package de.mhus.lib.vaadin.form;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.Form;

public class UiCheckbox extends UiVaadin {

	public UiCheckbox(Form form, IConfig config) {
		super(form, config);
	}

	@Override
	protected void setValue(Object value) throws MException {
		((CheckBox)getComponentError()).setValue(MCast.toboolean(value, false));
	}

	@Override
	public Component createEditor() {
		return new CheckBox();
	}

	@Override
	protected Object getValue() throws MException {
		return ((CheckBox)getComponentEditor()).getValue();
	}

}
