package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.Form;

public class UiTextArea extends UiVaadin {

	public UiTextArea(Form form, IConfig config) {
		super(form, config);
	}

	@Override
	protected void setValue(Object value) throws MException {
		((TextArea)getComponentError()).setValue(MCast.toString(value));
	}

	@Override
	public Component createEditor() {
		return new TextArea();
	}

	@Override
	protected Object getValue() throws MException {
		return ((TextArea)getComponentEditor()).getValue();
	}

}
