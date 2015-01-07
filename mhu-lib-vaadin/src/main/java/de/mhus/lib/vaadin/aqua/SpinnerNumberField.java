package de.mhus.lib.vaadin.aqua;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.ui.TextField;

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
