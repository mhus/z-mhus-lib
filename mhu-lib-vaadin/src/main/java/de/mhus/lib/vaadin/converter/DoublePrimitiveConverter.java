package de.mhus.lib.vaadin.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import de.mhus.lib.core.MCast;

public class DoublePrimitiveConverter implements Converter<String, Double> {

	private static final long serialVersionUID = 1L;

	@Override
	public Double convertToModel(String value,
			Class<? extends Double> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
			return MCast.todouble(value, 0);
	}

	@Override
	public String convertToPresentation(Double value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
		return String.valueOf(value);
	}

	@Override
	public Class<Double> getModelType() {
		return double.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
	
}
