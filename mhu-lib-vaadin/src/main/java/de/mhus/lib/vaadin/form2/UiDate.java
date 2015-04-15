package de.mhus.lib.vaadin.form2;

import java.util.Date;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;

import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataConnector;

public class UiDate extends UiText {

	

	@SuppressWarnings("rawtypes")
	@Override
	protected AbstractField createTextField() {
		return new DateField();
	}
	
	@Override
	protected void setValueToDataSource(DataConnector data) throws MException {
		data.setDate((Date)field.getValue());
	}
	
	@Override
	protected Object getValueFromDataSource(DataConnector data) {
		return data.getDate(null);
	}
	
	@Override
	protected void setValueToField(Object arg) {
		try {
			super.setValueToField(arg);
		} catch (Throwable t) {
			super.setValueToField(null);
		}
	}


}
