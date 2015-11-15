package de.mhus.lib.vaadin.form;

import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.Form;

public class UiText extends UiVaadin {

	public UiText(Form form, IConfig config) {
		super(form, config);
	}

	@Override
	protected void setValue(Object value) throws MException {
		
	}

	@Override
	protected void setCaption(String value) throws MException {
		
	}

	@Override
	protected Component[] getComponents() throws MException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEnabled(boolean enabled) throws MException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEnabled() throws MException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Component create(UiLayout grid) throws MException {
		UiRow row = grid.createRow();
		return null;
	}

}
