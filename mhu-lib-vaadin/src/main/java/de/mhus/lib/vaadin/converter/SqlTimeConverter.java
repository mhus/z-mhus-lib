package de.mhus.lib.vaadin.converter;

import java.sql.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;

public class SqlTimeConverter implements Converter<String, Date> {

	private static final long serialVersionUID = 1L;

	@Override
	public Date convertToModel(String value,
			Class<? extends Date> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
			return new Date(MCast.toDate(value, null).getTime());
	}

	@Override
	public String convertToPresentation(Date value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
    	if (value == null || ((Date) value).getTime() == 0) return "-";

        return  MDate.toTimeString( ((Date) value) );

	}

	@Override
	public Class<Date> getModelType() {
		return Date.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
	
}
