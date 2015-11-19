package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.Form;

public class UiDate extends UiVaadin {

	public UiDate(Form form, IConfig config) {
		super(form, config);
	}

	@Override
	protected void setValue(Object value) throws MException {
		((DateField)getComponentError()).setValue(MCast.toDate(value, null));
	}

	@Override
	public Component createEditor() {
		return new DateField();
	}

	@Override
	protected Object getValue() throws MException {
		return ((DateField)getComponentEditor()).getValue();
	}

}
