package de.mhus.lib.vaadin.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class StringConverter implements Converter<String, String> {

	@Override
	public String convertToModel(String value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		return value;
	}

	@Override
	public String convertToPresentation(String value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) return "";
		return String.valueOf(value);
	}

	@Override
	public Class<String> getModelType() {
		return String.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
