package de.mhus.lib.core;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;

import de.mhus.lib.errors.MException;

public interface IReadProperties {

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

	Number getNumber(String name, Number def);

	boolean isProperty(String name);

	Set<String> keys();

	Object get(Object name);

	Object getProperty(String name);

	boolean containsValue(Object value);

	Collection<Object> values();

	Set<Entry<String, Object>> entrySet();

}
