package de.mhus.lib.core;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

public abstract class IProperties extends MObject implements Iterable<Map.Entry<String,Object>> {

	/**
	 * Overwrite this function to provide values in string format.
	 * 
	 * @param name
	 * @return null if the property not exists or the property value.
	 * @throws MException 
	 */
	public abstract Object getProperty(String name);
	
	@Deprecated
	public int getProperty(String name, int def) throws MException {
		return getInt(name,def);
	}

//	@Deprecated
//	public boolean getProperty(String name, boolean def) {
//		return getBoolean(name, def);
//	}
//	
//	@Deprecated
//	public String getProperty(String name, String def) {
//		Object out = getProperty(name);
//		if (out == null) return def;
//		return String.valueOf(out);
//	}

	public String getString(String name, String def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		if (out == null) return def;
		return String.valueOf(out);
	}
	
	public String getString(String name) throws MException {
		Object out = getProperty(name);
		if (out == null) return null;
		return String.valueOf(out);
	}
	
	public boolean getBoolean(String name, boolean def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		if (out == null) return def;
		if (out instanceof Boolean) return ((Boolean)out).booleanValue();
		return MCast.toboolean(String.valueOf(out), def);
	}

	public boolean getBoolean(String name) throws MException {
		Object out = getProperty(name);
		if (out == null) throw new MException("value not found");
		if (out instanceof Boolean) return ((Boolean)out).booleanValue();
		return MCast.toboolean(String.valueOf(out), false);
	}

	public int getInt(String name, int def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		if (out == null) return def;
		if (out instanceof Number) return ((Number)out).intValue();
		return MCast.toint(String.valueOf(out),def);
	}
	
	public long getLong(String name, long def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		if (out == null) return def;
		if (out instanceof Number) return ((Number)out).longValue();
		return MCast.tolong(String.valueOf(out), def);
	}
	
	public float getFloat(String name, float def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		if (out == null) return def;
		if (out instanceof Number) return ((Number)out).floatValue();
		return MCast.tofloat(String.valueOf(out), def);
	}
	
	public double getDouble(String name, double def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		if (out == null) return def;
		if (out instanceof Number) return ((Number)out).doubleValue();
		return MCast.todouble(String.valueOf(out),def);
	}
	
	public Calendar getCalendar(String name) throws MException {
		Object out = getProperty(name);
		if (out == null) return null;
		if (out instanceof Calendar) return (Calendar)out;
		return MCast.toCalendar(String.valueOf(out));
	}
	
	public Date getDate(String name) {
		try {
			Object out = getProperty(name);
			if (out == null) return null;
			if (out instanceof Date) return (Date)out;
			if (out instanceof Calendar) return ((Calendar)out).getTime();
			
			return MCast.toDate(String.valueOf(out), null);
		} catch (Throwable t) {}
		return null;
	}
	
	public void setString(String name, String value) {
		setProperty(name, value);
	}
	
	public void setInt(String name, int value) {
		setProperty(name, value);
	}
	
	public void setLong(String name, long value) {
		setProperty(name, value);
	}

	public void setDouble(String name, double value) {
		setProperty(name, value);
	}

	public void setFloat(String name, float value) {
		setProperty(name, value);
	}
	
	public void setBoolean(String name, boolean value) {
		setProperty(name, value);
	}
	
	public void setCalendar(String name, Calendar value) {
		setProperty(name, value);
	}
	
	public void setDate(String name, Date value) {
		setProperty(name, value);
	}
	
	public void setNumber(String name, Number value) {
		if (value == null) {
			removeProperty(name);
			return;
		}
		if (value instanceof Integer)
			setInt(name, (Integer)value);
		else
		if (value instanceof Long) {
			setLong(name, (Long)value);
		} else
		if (value instanceof Float) {
			setFloat(name, (Float)value);
		} else
		if (value instanceof Double) {
			setDouble(name, (Double)value);
		} else
			throw new MRuntimeException("Unknown number class", name, value.getClass());
			
	}

	public Number getNumber(String name, Number def) {
		Object out = getProperty(name);
		if (out == null) return def;
		if (out instanceof Number) return (Number)out;
		try {
			return MCast.todouble(out, 0);
		} catch (NumberFormatException e) {
			return def;
		}
	}
	
	/**
	 * Return true if the property exists.
	 * 
	 * @param name
	 * @return
	 */
	public abstract boolean isProperty(String name);

	/**
	 * Remove the property field in the list of properties.
	 * 
	 * @param key
	 */
	public abstract void removeProperty(String key);
	
	/**
	 * Overwrite this function to allow changes in properties.
	 * 
	 * @param key
	 * @param value
	 */
	public abstract void setProperty(String key, Object value);
	
	/**
	 * Overwrite this function and return true if the property set can be edited.
	 * 
	 * @return
	 */
	public abstract boolean isEditable();
	
	/**
	 * 
	 */
	public abstract Set<String> keys();
	
	public Iterator<Map.Entry<String, Object>> iterator() {
		return new IPIterator();
	}
	
	private class IPIterator implements Iterator<Map.Entry<String, Object>> {

		private Iterator<String> keys;
		private String currentkey;

		IPIterator() {
			keys = keys().iterator();
		}
		
		@Override
		public boolean hasNext() {
			return keys.hasNext();
		}

		@Override
		public Entry<String, Object> next() {
			currentkey = keys.next();
			return new IPEntry(currentkey);
		}

		@Override
		public void remove() {
			try {
				removeProperty(currentkey);
			} catch (Throwable e) {
				log().t(e);
			}
		}
	}
	
	private class IPEntry implements Map.Entry<String, Object> {

		private String key;

		public IPEntry(String next) {
			key = next;
		}

		@Override
		public String getKey() {
			return key;
		}

		@Override
		public Object getValue() {
			try {
				return getProperty(key);
			} catch (Throwable e) {
				throw new MRuntimeException(e);
			}
		}

		@Override
		public Object setValue(Object value) {
			Object old = null;
			try {
				old = getProperty(key);
			} catch (Throwable e1) {
				log().t(key,e1);
			}
			try {
				setProperty(key, value);
			} catch (Throwable e) {
				log().t(key,e);
			}
			return old;
		}
		
	}
}
