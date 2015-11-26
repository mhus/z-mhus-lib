package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.Form;
import de.mhus.lib.form.UiComponent;

public class UiText extends UiVaadin {

	@Override
	protected void setValue(Object value) throws MException {
		((TextField)getComponentEditor()).setValue(MCast.toString(value));
	}

	@Override
	public Component createEditor() {
		return new TextField();
	}

	@Override
	protected Object getValue() throws MException {
		return ((TextField)getComponentEditor()).getValue();
	}

	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiText();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
