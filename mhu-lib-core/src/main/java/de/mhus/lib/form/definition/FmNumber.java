package de.mhus.lib.form.definition;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;

public class FmNumber extends FmElement {
	public enum TYPES {INTEGER,LONG,FLOAT,DOUBLE}
	public enum FORMAT {PERCENTAGE,CURRENCY}

	public static final String TYPE_NUMBER = "number";
	public static final String ALLOW_NEGATIVE = "allow_negative";
	public static final String MINIMUM = "min";
	public static final String MAXIMUM = "max";
	public static final String FORMAT = "format";
	public static final String NUMBER_TYPE = "number";

	public FmNumber(String name, TYPES type, String title, String description) {
		this(name, type, new FmNls(title, description));
	}

	public FmNumber(String name, TYPES type, IDefAttribute ... definitions) {
		super(name, definitions);
		setString(FmElement.TYPE,TYPE_NUMBER);
		setString(NUMBER_TYPE,type.name().toLowerCase());
	}
	
	public FmNumber allowNull(boolean in) throws MException {
		setBoolean(FmElement.ALLOW_NULL, in);
		return this;
	}
	
	public FmNumber allowNegative(boolean in) throws MException {
		setBoolean(ALLOW_NEGATIVE, in);
		return this;
	}
	
	public FmNumber min(int min) throws MException {
		setInt(MINIMUM, min);
		return this;
	}

	public FmNumber max(int max) throws MException {
		setInt(MAXIMUM, max);
		return this;
	}
	
	public FmNumber format(FORMAT format) throws MException {
		setString(FORMAT, format.name());
		return this;
	}
}
