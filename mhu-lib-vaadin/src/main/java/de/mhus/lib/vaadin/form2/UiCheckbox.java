package de.mhus.lib.vaadin.form2;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;

import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataConnector;
import de.mhus.lib.form.definition.FmElement;

public class UiCheckbox extends UiText {

	private boolean def;

	@SuppressWarnings("rawtypes")
	@Override
	protected AbstractField createTextField() throws MException {
		def = getElement().getConfig().getBoolean(FmElement.DEFAULT,false);

		return new CheckBox();
	}
	
	@Override
	protected void setValueToDataSource(DataConnector data) throws MException {
		try {
			data.setBoolean((Boolean)field.getValue());
		} catch (Throwable t) {
			
		}
	}
	
	@Override
	protected Object getValueFromDataSource(DataConnector data) throws MException {
		return data.getBoolean(def);
	}
	
}
