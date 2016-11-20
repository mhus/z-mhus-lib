package de.mhus.lib.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.errors.MException;

public interface IProperties extends IReadProperties, Map<String,Object>, Serializable, Iterable<Map.Entry<String,Object>> {


	void setString(String name, String value);

	void setInt(String name, int value);

	void setLong(String name, long value);

	void setDouble(String name, double value);

	void setFloat(String name, float value);

	void setBoolean(String name, boolean value);

	void setCalendar(String name, Calendar value);

	void setDate(String name, Date value);

	void setNumber(String name, Number value);

	void removeProperty(String key);

	boolean isEditable();
	
	@Override
	void clear();

}
