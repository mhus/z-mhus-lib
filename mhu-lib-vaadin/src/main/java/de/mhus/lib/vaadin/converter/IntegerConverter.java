package de.mhus.lib.vaadin.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import de.mhus.lib.core.MCast;

public class IntegerConverter implements Converter<String, Integer> {

	private static final long serialVersionUID = 1L;

	@Override
	public Integer convertToModel(String value,
			Class<? extends Integer> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
			return MCast.toint(value, 0);
	}

	@Override
	public String convertToPresentation(Integer value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
		return String.valueOf(value);
	}

	@Override
	public Class<Integer> getModelType() {
		return Integer.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
	
}
