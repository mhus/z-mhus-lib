package de.mhus.lib.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

/**
 * <p>Abstract IProperties class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class IProperties extends MObject implements Iterable<Map.Entry<String,Object>>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Overwrite this function to provide values in string format.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return null if the property not exists or the property value.
	 */
	public abstract Object getProperty(String name);
	
	/**
	 * <p>getProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a int.
	 * @return a int.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
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

	/**
	 * <p>getString.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
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
	
	/**
	 * <p>getString.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public String getString(String name) throws MException {
		Object out = getProperty(name);
		if (out == null) return null;
		return String.valueOf(out);
	}
	
	/**
	 * <p>getBoolean.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a boolean.
	 * @return a boolean.
	 */
	public boolean getBoolean(String name, boolean def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		return MCast.toboolean(out, def);
	}

	/**
	 * <p>getBoolean.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public boolean getBoolean(String name) throws MException {
		Object out = getProperty(name);
		if (out == null) throw new MException("value not found");
		return MCast.toboolean(out, false);
	}

	/**
	 * <p>getInt.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a int.
	 * @return a int.
	 */
	public int getInt(String name, int def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		return MCast.toint(out,def);
	}
	
	/**
	 * <p>getLong.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a long.
	 * @return a long.
	 */
	public long getLong(String name, long def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		return MCast.tolong(out, def);
	}
	
	/**
	 * <p>getFloat.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a float.
	 * @return a float.
	 */
	public float getFloat(String name, float def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		return MCast.tofloat(out, def);
	}
	
	/**
	 * <p>getDouble.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a double.
	 * @return a double.
	 */
	public double getDouble(String name, double def) {
		Object out;
		try {
			out = getProperty(name);
		} catch (Throwable e) {
			return def;
		}
		return MCast.todouble(out,def);
	}
	
	/**
	 * <p>getCalendar.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.util.Calendar} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	public Calendar getCalendar(String name) throws MException {
		Object out = getProperty(name);
		return MCast.toCalendar(out);
	}
	
	/**
	 * <p>getDate.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.util.Date} object.
	 */
	public Date getDate(String name) {
		try {
			Object out = getProperty(name);
			return MCast.toDate(out, null);
		} catch (Throwable t) {}
		return null;
	}
	
	/**
	 * <p>setString.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 */
	public void setString(String name, String value) {
		setProperty(name, value);
	}
	
	/**
	 * <p>setInt.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a int.
	 */
	public void setInt(String name, int value) {
		setProperty(name, value);
	}
	
	/**
	 * <p>setLong.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a long.
	 */
	public void setLong(String name, long value) {
		setProperty(name, value);
	}

	/**
	 * <p>setDouble.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a double.
	 */
	public void setDouble(String name, double value) {
		setProperty(name, value);
	}

	/**
	 * <p>setFloat.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a float.
	 */
	public void setFloat(String name, float value) {
		setProperty(name, value);
	}
	
	/**
	 * <p>setBoolean.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a boolean.
	 */
	public void setBoolean(String name, boolean value) {
		setProperty(name, value);
	}
	
	/**
	 * <p>setCalendar.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.util.Calendar} object.
	 */
	public void setCalendar(String name, Calendar value) {
		setProperty(name, value);
	}
	
	/**
	 * <p>setDate.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.util.Date} object.
	 */
	public void setDate(String name, Date value) {
		setProperty(name, value);
	}
	
	/**
	 * <p>setNumber.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Number} object.
	 */
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

	/**
	 * <p>getNumber.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param def a {@link java.lang.Number} object.
	 * @return a {@link java.lang.Number} object.
	 */
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
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public abstract boolean isProperty(String name);

	/**
	 * Remove the property field in the list of properties.
	 *
	 * @param key a {@link java.lang.String} object.
	 */
	public abstract void removeProperty(String key);
	
	/**
	 * Overwrite this function to allow changes in properties.
	 *
	 * @param key a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 */
	public abstract void setProperty(String key, Object value);
	
	/**
	 * Overwrite this function and return true if the property set can be edited.
	 *
	 * @return a boolean.
	 */
	public abstract boolean isEditable();
	
	/**
	 * <p>keys.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public abstract Set<String> keys();
	
	/** {@inheritDoc} */
	@Override
	public Iterator<Map.Entry<String, Object>> iterator() {
		return new IPIterator();
	}

	/**
	 * <p>toMap.</p>
	 *
	 * @return a {@link java.util.Map} object.
	 * @since 3.2.9
	 */
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
}
