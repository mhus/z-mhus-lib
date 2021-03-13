package de.mhus.lib.core.config;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotFoundException;

public class MConfigWrapper extends MConfig {

	private IProperties parameters;

	public MConfigWrapper(IProperties parameters) {
		this.parameters = parameters;
	}

	public String getString(String name, String def) {
		return parameters.getString(name, def);
	}

	public String getStringOrCreate(String name, Function<String, String> def) {
		return parameters.getStringOrCreate(name, def);
	}

	public void setProperty(String name, Object value) {
		parameters.setProperty(name, value);
	}

	public String getString(String name) throws NotFoundException {
		try {
			return parameters.getString(name);
		} catch (MException e) {
			throw new NotFoundException(name,e);
		}
	}

	public void setString(String name, String value) {
		parameters.setString(name, value);
	}

	public boolean getBoolean(String name, boolean def) {
		return parameters.getBoolean(name, def);
	}

	public void setInt(String name, int value) {
		parameters.setInt(name, value);
	}

	public boolean getBoolean(String name) throws NotFoundException {
		try {
			return parameters.getBoolean(name);
		} catch (MException e) {
			throw new NotFoundException(name,e);
		}
	}

	public void setLong(String name, long value) {
		parameters.setLong(name, value);
	}

	public void setDouble(String name, double value) {
		parameters.setDouble(name, value);
	}

	public void setFloat(String name, float value) {
		parameters.setFloat(name, value);
	}

	public int getInt(String name, int def) {
		return parameters.getInt(name, def);
	}

	public void setBoolean(String name, boolean value) {
		parameters.setBoolean(name, value);
	}

	public long getLong(String name, long def) {
		return parameters.getLong(name, def);
	}

	public void setCalendar(String name, Calendar value) {
		parameters.setCalendar(name, value);
	}

	public void setDate(String name, Date value) {
		parameters.setDate(name, value);
	}

	public float getFloat(String name, float def) {
		return parameters.getFloat(name, def);
	}

	public void setNumber(String name, Number value) {
		parameters.setNumber(name, value);
	}

	public void removeProperty(String key) {
		parameters.removeProperty(key);
	}

	public boolean isEditable() {
		return parameters.isEditable();
	}

	public double getDouble(String name, double def) {
		return parameters.getDouble(name, def);
	}

	public void clear() {
		parameters.clear();
	}

	public String getFormatted(String name, String def, Object... values) {
		return parameters.getFormatted(name, def, values);
	}

	public Iterator<Entry<String, Object>> iterator() {
		return parameters.iterator();
	}

	public Calendar getCalendar(String name) throws MException {
		return parameters.getCalendar(name);
	}

	public void forEach(Consumer<? super Entry<String, Object>> action) {
		parameters.forEach(action);
	}

	public Date getDate(String name) {
		return parameters.getDate(name);
	}

	public Number getNumber(String name, Number def) {
		return parameters.getNumber(name, def);
	}

	public boolean isProperty(String name) {
		return parameters.isProperty(name);
	}

	public Set<String> keys() {
		return parameters.keys();
	}

	public Object get(Object name) {
		return parameters.get(name);
	}

	public Object getProperty(String name) {
		return parameters.getProperty(name);
	}

	public boolean containsValue(Object value) {
		return parameters.containsValue(value);
	}

	public boolean containsKey(Object key) {
		return parameters.containsKey(key);
	}

	public Collection<Object> values() {
		return parameters.values();
	}

	public Set<Entry<String, Object>> entrySet() {
		return parameters.entrySet();
	}

	public Spliterator<Entry<String, Object>> spliterator() {
		return parameters.spliterator();
	}

	public int size() {
		return parameters.size();
	}

	public boolean isEmpty() {
		return parameters.isEmpty();
	}

	public Object put(String key, Object value) {
		return parameters.put(key, value);
	}

	public Object remove(Object key) {
		return parameters.remove(key);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		parameters.putAll(m);
	}

	public Set<String> keySet() {
		return parameters.keySet();
	}

	public boolean equals(Object o) {
		return parameters.equals(o);
	}

	public int hashCode() {
		return parameters.hashCode();
	}

	public Object getOrDefault(Object key, Object defaultValue) {
		return parameters.getOrDefault(key, defaultValue);
	}

	public void forEach(BiConsumer<? super String, ? super Object> action) {
		parameters.forEach(action);
	}

	public void replaceAll(BiFunction<? super String, ? super Object, ? extends Object> function) {
		parameters.replaceAll(function);
	}

	public Object putIfAbsent(String key, Object value) {
		return parameters.putIfAbsent(key, value);
	}

	public boolean remove(Object key, Object value) {
		return parameters.remove(key, value);
	}

	public boolean replace(String key, Object oldValue, Object newValue) {
		return parameters.replace(key, oldValue, newValue);
	}

	public Object replace(String key, Object value) {
		return parameters.replace(key, value);
	}

	public Object computeIfAbsent(String key, Function<? super String, ? extends Object> mappingFunction) {
		return parameters.computeIfAbsent(key, mappingFunction);
	}

	public Object computeIfPresent(String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return parameters.computeIfPresent(key, remappingFunction);
	}

	public Object compute(String key, BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return parameters.compute(key, remappingFunction);
	}

	public Object merge(String key, Object value,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		return parameters.merge(key, value, remappingFunction);
	}

}
