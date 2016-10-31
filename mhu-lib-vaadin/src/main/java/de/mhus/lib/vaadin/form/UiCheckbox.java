package de.mhus.lib.vaadin.form;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.ComponentAdapter;
import de.mhus.lib.form.ComponentDefinition;
import de.mhus.lib.form.UiComponent;

public class UiCheckbox extends UiVaadin {

	@Override
	protected void setValue(Object value) throws MException {
		((CheckBox)getComponentEditor()).setValue(MCast.toboolean(value, false));
	}

	@Override
	public Component createEditor() {
		return new CheckBox();
	}

	@Override
	protected Object getValue() throws MException {
		return ((CheckBox)getComponentEditor()).getValue();
	}

	public static class Adapter implements ComponentAdapter {

		@Override
		public UiComponent createAdapter(IConfig config) {
			return new UiCheckbox();
		}

		@Override
		public ComponentDefinition getDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
