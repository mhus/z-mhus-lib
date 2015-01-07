package de.mhus.lib.vaadin.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import de.mhus.lib.core.MCast;

public class FloatPrimitiveConverter implements Converter<String, Float> {

	private static final long serialVersionUID = 1L;

	@Override
	public Float convertToModel(String value,
			Class<? extends Float> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
			return MCast.tofloat(value, 0);
	}

	@Override
	public String convertToPresentation(Float value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
		return String.valueOf(value);
	}

	@Override
	public Class<Float> getModelType() {
		return float.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
	
}
