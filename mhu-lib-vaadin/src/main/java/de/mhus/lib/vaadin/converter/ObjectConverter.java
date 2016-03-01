package de.mhus.lib.vaadin.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class ObjectConverter implements Converter<String, Object> {

	@Override
	public Object convertToModel(String value,
			Class<? extends Object> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {

		if (value == null) return null;
		return String.valueOf(value);
	}

	@Override
	public String convertToPresentation(Object value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) return "";
		return String.valueOf(value);
	}

	@Override
	public Class<Object> getModelType() {
		return Object.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
