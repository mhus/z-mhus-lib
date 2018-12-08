/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.vaadin.converter;

import java.util.Locale;

import com.vaadin.v7.data.util.converter.Converter;

import de.mhus.lib.core.MCast;

@SuppressWarnings("deprecation")
public class FloatPrimitiveConverter implements Converter<String, Float> {

	private static final long serialVersionUID = 1L;

	@Override
	public Float convertToModel(String value,
			Class<? extends Float> targetType, Locale locale)
			throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
			return MCast.tofloat(value, 0);
	}

	@Override
	public String convertToPresentation(Float value,
			Class<? extends String> targetType, Locale locale)
			throws com.vaadin.v7.data.util.converter.Converter.ConversionException {
		
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
