package de.mhus.lib.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.errors.MException;

public interface IProperties extends Map<String,Object>, Serializable, Iterable<Map.Entry<String,Object>> {

	String getString(String name, String def);

	String getString(String name) throws MException;

	boolean getBoolean(String name, boolean def);

	boolean getBoolean(String name) throws MException;

	int getInt(String name, int def);

	long getLong(String name, long def);

	float getFloat(String name, float def);

	double getDouble(String name, double def);

	Calendar getCalendar(String name) throws MException;

	Date getDate(String name);

	void setString(String name, String value);

	void setInt(String name, int value);

	void setLong(String name, long value);

	void setDouble(String name, double value);

	void setFloat(String name, float value);

	void setBoolean(String name, boolean value);

	void setCalendar(String name, Calendar value);

	void setDate(String name, Date value);

	void setNumber(String name, Number value);

	Number getNumber(String name, Number def);

	boolean isProperty(String name);

	void removeProperty(String key);

	boolean isEditable();

	Set<String> keys();
	
}
