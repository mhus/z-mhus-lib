package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmElement;

public class FmNumber extends FmElement {
	public enum TYPE {INTEGER,LONG,FLOAT,DOUBLE}
	public enum FORMAT {PERCENTAGE,CURRENCY}

	public FmNumber(String name, TYPE type, IDefAttribute ... definitions) {
		super(name, definitions);
		setString("type","number");
		setString("number",type.name().toLowerCase());
	}
	
	public FmNumber allowNull(boolean in) throws MException {
		setBoolean("allow_null", in);
		return this;
	}
	
	public FmNumber allowNegative(boolean in) throws MException {
		setBoolean("allow_negative", in);
		return this;
	}
	
	public FmNumber min(int min) throws MException {
		setInt("min", min);
		return this;
	}

	public FmNumber max(int max) throws MException {
		setInt("max", max);
		return this;
	}
	
	public FmNumber format(FORMAT format) throws MException {
		setString("format", format.name());
		return this;
	}
}
