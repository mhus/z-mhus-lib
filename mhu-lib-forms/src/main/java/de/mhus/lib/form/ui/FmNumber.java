package de.mhus.lib.form.ui;

import de.mhus.lib.core.definition.IDefAttribute;
import de.mhus.lib.errors.MException;
import de.mhus.lib.form.definition.FmDefaultSources;
import de.mhus.lib.form.definition.FmElement;
import de.mhus.lib.form.definition.FmNls;

/**
 * <p>FmNumber class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FmNumber extends FmElement {
	public enum TYPES {INTEGER,LONG,FLOAT,DOUBLE}
	public enum FORMAT {PERCENTAGE,CURRENCY}

	/** Constant <code>TYPE_NUMBER="number"</code> */
	public static final String TYPE_NUMBER = "number";
	/** Constant <code>ALLOW_NEGATIVE="allow_negative"</code> */
	public static final String ALLOW_NEGATIVE = "allow_negative";
	/** Constant <code>MINIMUM="min"</code> */
	public static final String MINIMUM = "min";
	/** Constant <code>MAXIMUM="max"</code> */
	public static final String MAXIMUM = "max";
	/** Constant <code>FORMAT="format"</code> */
	public static final String FORMAT = "format";
	/** Constant <code>NUMBER_TYPE="number"</code> */
	public static final String NUMBER_TYPE = "number";

	/**
	 * <p>Constructor for FmNumber.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param type a {@link de.mhus.lib.form.ui.FmNumber.TYPES} object.
	 * @param title a {@link java.lang.String} object.
	 * @param description a {@link java.lang.String} object.
	 */
	public FmNumber(String name, TYPES type, String title, String description) {
		this(name, type, new FmNls(title, description), new FmDefaultSources());
	}

	/**
	 * <p>Constructor for FmNumber.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param type a {@link de.mhus.lib.form.ui.FmNumber.TYPES} object.
	 * @param definitions a {@link de.mhus.lib.core.definition.IDefAttribute} object.
	 */
	public FmNumber(String name, TYPES type, IDefAttribute ... definitions) {
		super(name, definitions);
		setString(FmElement.TYPE,TYPE_NUMBER);
		setString(NUMBER_TYPE,type.name().toLowerCase());
	}
	
	/**
	 * <p>allowNull.</p>
	 *
	 * @param in a boolean.
	 * @return a {@link de.mhus.lib.form.ui.FmNumber} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public FmNumber allowNull(boolean in) throws MException {
		setBoolean(FmElement.ALLOW_NULL, in);
		return this;
	}
	
	/**
	 * <p>allowNegative.</p>
	 *
	 * @param in a boolean.
	 * @return a {@link de.mhus.lib.form.ui.FmNumber} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public FmNumber allowNegative(boolean in) throws MException {
		setBoolean(ALLOW_NEGATIVE, in);
		return this;
	}
	
	/**
	 * <p>min.</p>
	 *
	 * @param min a int.
	 * @return a {@link de.mhus.lib.form.ui.FmNumber} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public FmNumber min(int min) throws MException {
		setInt(MINIMUM, min);
		return this;
	}

	/**
	 * <p>max.</p>
	 *
	 * @param max a int.
	 * @return a {@link de.mhus.lib.form.ui.FmNumber} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public FmNumber max(int max) throws MException {
		setInt(MAXIMUM, max);
		return this;
	}
	
	/**
	 * <p>format.</p>
	 *
	 * @param format a {@link de.mhus.lib.form.ui.FmNumber.FORMAT} object.
	 * @return a {@link de.mhus.lib.form.ui.FmNumber} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public FmNumber format(FORMAT format) throws MException {
		setString(FORMAT, format.name());
		return this;
	}
}
