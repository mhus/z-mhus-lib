package de.mhus.lib.vaadin.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MDate;

public class DateTimeConverter implements Converter<String, Date> {

	private static final long serialVersionUID = 1L;

	@Override
	public Date convertToModel(String value,
			Class<? extends Date> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
			return MCast.toDate(value, null);
	}

	@Override
	public String convertToPresentation(Date value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
    	if (value == null || ((Date) value).getTime() == 0) return "-";
        return MDate.toDateTimeString( ((Date) value) );
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
