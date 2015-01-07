package de.mhus.lib.vaadin.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import de.mhus.lib.core.MCast;

public class BooleanPrimitiveConverter implements Converter<String, Boolean> {

	private static final long serialVersionUID = 1L;

	@Override
	public Boolean convertToModel(String value,
			Class<? extends Boolean> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
			return MCast.toboolean(value, false);
	}

	@Override
	public String convertToPresentation(Boolean value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
		return value.booleanValue() ? "\u2612" : "\u2610";
	}

	@Override
	public Class<Boolean> getModelType() {
		return boolean.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
	
}
