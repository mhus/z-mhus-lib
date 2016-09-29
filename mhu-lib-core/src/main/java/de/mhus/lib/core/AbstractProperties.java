package de.mhus.lib.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

public abstract class AbstractProperties extends MObject implements IProperties {

	private static final long serialVersionUID = 1L;

	/**
	 * Overwrite this function to provide values in string format.
	 * 
	 * @param name
	 * @return null if the property not exists or the property value.
	 * @throws MException 
	 */
	public abstract Object getProperty(String name);
	
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

	@Override
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
	
	@Override
	public String getString(String name) throws MException {
		Object out = getProperty(name);
		if (out == null) return null;
		return String.valueOf(out);
	}
	
	@Override
	public boolean getBoolean(String name, boolean def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		return MCast.toboolean(out, def);
	}

	@Override
	public boolean getBoolean(String name) throws MException {
		Object out = getProperty(name);
		if (out == null) throw new MException("value not found");
		return MCast.toboolean(out, false);
	}

	@Override
	public int getInt(String name, int def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		return MCast.toint(out,def);
	}
	
	@Override
	public long getLong(String name, long def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		return MCast.tolong(out, def);
	}
	
	@Override
	public float getFloat(String name, float def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		return MCast.tofloat(out, def);
	}
	
	@Override
	public double getDouble(String name, double def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		return MCast.todouble(out,def);
	}
	
	@Override
	public Calendar getCalendar(String name) throws MException {
		Object out = getProperty(name);
		return MCast.toCalendar(out);
	}
	
	@Override
	public Date getDate(String name) {
		try {
			Object out = getProperty(name);
			return MCast.toDate(out, null);
		} catch (Throwable t) {}
		return null;
	}
	
	@Override
	public void setString(String name, String value) {
		setProperty(name, value);
	}
	
	@Override
	public void setInt(String name, int value) {
		setProperty(name, value);
	}
	
	@Override
	public void setLong(String name, long value) {
		setProperty(name, value);
	}

	@Override
	public void setDouble(String name, double value) {
		setProperty(name, value);
	}

	@Override
	public void setFloat(String name, float value) {
		setProperty(name, value);
	}
	
	@Override
	public void setBoolean(String name, boolean value) {
		setProperty(name, value);
	}
	
	@Override
	public void setCalendar(String name, Calendar value) {
		setProperty(name, value);
	}
	
	@Override
	public void setDate(String name, Date value) {
		setProperty(name, value);
	}
	
	@Override
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

	@Override
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
	@Override
	public abstract boolean isProperty(String name);

	/**
	 * Remove the property field in the list of properties.
	 * 
	 * @param key
	 */
	@Override
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
	@Override
	public abstract boolean isEditable();
	
	/**
	 * @return 
	 * 
	 */
	@Override
	public abstract Set<String> keys();
	
	@Override
	public Iterator<Map.Entry<String, Object>> iterator() {
		return new IPIterator();
	}

	public Map<String, Object> toMap() {
		Map<String, Object> out = new HashMap<>();
		for (Map.Entry<String, Object> entry : this) {
			out.put(entry.getKey(), entry.getValue());
		}
		return out;
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

	@Override
	public abstract int size();
	
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		if (key == null) return false;
		return isProperty(String.valueOf(key));
	}

	@Override
	public Object get(Object key) {
		if (key == null) return null;
		return getProperty(String.valueOf(key));
	}

	@Override
	public Object put(String key, Object value) {
		Object current = get(key);
		setProperty(key, value);
		return current;
	}

	@Override
	public Object remove(Object key) {
		if (key == null) return null;
		Object current = get(key);
		removeProperty(String.valueOf(key));
		return current;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		for (Map.Entry<? extends String, ? extends Object> e : m.entrySet())
			put(e.getKey(),e.getValue());
	}

	@Override
	public void clear() {
		for (String name : keys())
			removeProperty(name);
	}

	@Override
	public Set<String> keySet() {
		return keys();
	}

}
