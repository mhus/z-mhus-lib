package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.Form;

public class UiRichTextArea extends UiVaadin {

	public UiRichTextArea(Form form, IConfig config) {
		super(form, config);
	}

	@Override
	protected void setValue(Object value) throws MException {
		((RichTextArea)getComponentError()).setValue(MCast.toString(value));
	}

	@Override
	public Component createEditor() {
		return new RichTextArea();
	}

	@Override
	protected Object getValue() throws MException {
		return ((RichTextArea)getComponentEditor()).getValue();
	}

}
