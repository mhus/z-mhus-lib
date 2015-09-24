package de.mhus.lib.vaadin.form2;

import java.util.Date;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.DataConnector;
import de.mhus.lib.form.DataValidationException;

public class UiDate extends UiText {

	

	@SuppressWarnings("rawtypes")
	@Override
	protected AbstractField createTextField() {
	//	return new DateField();
		return new TextField();
	}
	
	@Override
	protected void setValueToDataSource(DataConnector data) throws MException {
//		data.setDate((Date)field.getValue());
		Object value = field.getValue();
		if ("".equals(value))
			data.setDate(null);
		else {
			Date date = MCast.toDate(value, null);
			if (date == null)
				throw new DataValidationException("invalid date");
			data.setDate(date);
		}
	}
	
	@Override
	protected Object getValueFromDataSource(DataConnector data) {
		return data.getDate(null);
	}
	 
	@Override
	protected void setValueToField(Object arg) {
		try {
			
			if (arg instanceof Date) {
				arg = MDate.toLocaleString( (Date)arg, false);
				if (arg == null) arg = "";
			} else {
				arg = "";
			}
			super.setValueToField(arg);
		} catch (Throwable t) {
			super.setValueToField(null);
		}
	}


}
