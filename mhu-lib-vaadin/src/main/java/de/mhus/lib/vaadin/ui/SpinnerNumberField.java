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
package de.mhus.lib.vaadin.ui;

import com.vaadin.v7.data.validator.AbstractStringValidator;
import com.vaadin.v7.ui.TextField;

@SuppressWarnings("deprecation")
public class SpinnerNumberField<T> extends TextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean allowNegative;
	private long max;
	private long min;
	private boolean allowNull;
	private Class<? extends Number> numberClass;
	private SpinnerValidator validator;
	private boolean maxSet;
	private boolean minSet;

	public SpinnerNumberField(Class<? extends Number> numberClass) {
		this.numberClass = numberClass;
		validator = new SpinnerValidator("Insert a valid number.");
		addValidator(validator);
		// https://stackoverflow.com/questions/42491883/how-to-add-validators-in-vaadin-8
	}

	public void setAllowNegative(boolean boolean1) {
		allowNegative = boolean1;
	}

	public void setAllowNull(boolean boolean1) {
		allowNull = boolean1;
	}

	public void setMaxVal(long long1) {
		max = long1;
		maxSet = true;
	}

	public void setMinVal(long long1) {
		min = long1;
		minSet = true;
	}
	
	private class SpinnerValidator  extends AbstractStringValidator {

		public SpinnerValidator(String errorMessage) {
			super(errorMessage);
		}

		private static final long serialVersionUID = 1L;

		@Override
		protected boolean isValidValue(String value) {
			if (allowNull && value == null) return true;
			try {
				Number n = getNumberValue(value);
				if (!allowNegative && n.longValue() < 0)
					return false;
				
				if (minSet && n.longValue() < min)
					return false;
				
				if (maxSet && n.longValue() > max)
					return false;
				
			} catch (Throwable e) {
	            return false;
	        }
			return true;
		}
		
	}

	public Number getNumberValue() {
		return getNumberValue(String.valueOf(getValue()));
	}
	
	protected Number getNumberValue(String value) {
		if (value.endsWith(".0")) value = value.substring(0,value.length()-2);
		Number n = null;
		if (numberClass == Double.class)
			n = Double.parseDouble(value);
		else
		if (numberClass == Float.class)
			n = Float.parseFloat(value);
		else
		if (numberClass == Long.class)
			n = Long.parseLong(value);
		else
			n = Integer.parseInt(value);
		return n;
	}
}
