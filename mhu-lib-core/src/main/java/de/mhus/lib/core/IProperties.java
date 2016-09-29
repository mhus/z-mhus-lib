package de.mhus.lib.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.errors.MException;

/**
 * <p>IProperties interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface IProperties extends Map<String,Object>, Serializable, Iterable<Map.Entry<String,Object>> {

	/**
	 * <p>getString.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String getString(String name, String def);

	/**
	 * <p>getString.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	String getString(String name) throws MException;

	/**
	 * <p>getBoolean.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a boolean.
	 * @return a boolean.
	 */
	boolean getBoolean(String name, boolean def);

	/**
	 * <p>getBoolean.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	boolean getBoolean(String name) throws MException;

	/**
	 * <p>getInt.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a int.
	 * @return a int.
	 */
	int getInt(String name, int def);

	/**
	 * <p>getLong.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a long.
	 * @return a long.
	 */
	long getLong(String name, long def);

	/**
	 * <p>getFloat.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a float.
	 * @return a float.
	 */
	float getFloat(String name, float def);

	/**
	 * <p>getDouble.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a double.
	 * @return a double.
	 */
	double getDouble(String name, double def);

	/**
	 * <p>getCalendar.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.util.Calendar} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	Calendar getCalendar(String name) throws MException;

	/**
	 * <p>getDate.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.util.Date} object.
	 */
	Date getDate(String name);

	/**
	 * <p>setString.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 */
	void setString(String name, String value);

	/**
	 * <p>setInt.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a int.
	 */
	void setInt(String name, int value);

	/**
	 * <p>setLong.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a long.
	 */
	void setLong(String name, long value);

	/**
	 * <p>setDouble.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a double.
	 */
	void setDouble(String name, double value);

	/**
	 * <p>setFloat.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a float.
	 */
	void setFloat(String name, float value);

	/**
	 * <p>setBoolean.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a boolean.
	 */
	void setBoolean(String name, boolean value);

	/**
	 * <p>setCalendar.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.util.Calendar} object.
	 */
	void setCalendar(String name, Calendar value);

	/**
	 * <p>setDate.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.util.Date} object.
	 */
	void setDate(String name, Date value);

	/**
	 * <p>setNumber.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Number} object.
	 */
	void setNumber(String name, Number value);

	/**
	 * <p>getNumber.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a {@link java.lang.Number} object.
	 * @return a {@link java.lang.Number} object.
	 */
	Number getNumber(String name, Number def);

	/**
	 * <p>isProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	boolean isProperty(String name);

	/**
	 * <p>removeProperty.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 */
	void removeProperty(String key);

	/**
	 * <p>isEditable.</p>
	 *
	 * @return a boolean.
	 */
	boolean isEditable();

	/**
	 * <p>keys.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	Set<String> keys();
	
}
