package de.mhus.lib.vaadin.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import de.mhus.lib.core.MCast;

public class LongPrimitiveConverter implements Converter<String, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long convertToModel(String value,
			Class<? extends Long> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
			return MCast.tolong(value, 0);
	}

	@Override
	public String convertToPresentation(Long value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
		return String.valueOf(value);
	}

	@Override
	public Class<Long> getModelType() {
		return long.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}
	
}
