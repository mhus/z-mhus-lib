package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.UiComponent;

public class UiTextArea extends UiVaadin {

	@Override
	protected void setValue(Object value) throws MException {
		((TextArea)getComponentEditor()).setValue(MCast.toString(value));
	}

	@Override
	public Component createEditor() {
		TextArea ret = new TextArea();
		ret.setHeight("100px");
		return ret;
	}

	@Override
	protected Object getValue() throws MException {
		return ((TextArea)getComponentEditor()).getValue();
	}

	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiTextArea();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
