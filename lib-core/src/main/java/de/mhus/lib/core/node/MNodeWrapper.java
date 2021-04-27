package de.mhus.lib.core.node;

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

public class MNodeWrapper extends MNode {

	private IProperties parameters;

	public MNodeWrapper(IProperties parameters) {
		this.parameters = parameters;
	}

	@Override
    public String getString(String name, String def) {
		return parameters.getString(name, def);
	}

    @Override
	public String getStringOrCreate(String name, Function<String, String> def) {
		return parameters.getStringOrCreate(name, def);
	}

    @Override
	public void setProperty(String name, Object value) {
		parameters.setProperty(name, value);
	}

    @Override
	public String getString(String name) throws NotFoundException {
		try {
			return parameters.getString(name);
		} catch (MException e) {
			throw new NotFoundException(name,e);
		}
	}

    @Override
	public void setString(String name, String value) {
		parameters.setString(name, value);
	}

    @Override
	public boolean getBoolean(String name, boolean def) {
		return parameters.getBoolean(name, def);
	}

    @Override
	public void setInt(String name, int value) {
		parameters.setInt(name, value);
	}

    @Override
	public boolean getBoolean(String name) throws NotFoundException {
		try {
			return parameters.getBoolean(name);
		} catch (MException e) {
			throw new NotFoundException(name,e);
		}
	}

    @Override
	public void setLong(String name, long value) {
		parameters.setLong(name, value);
	}

    @Override
	public void setDouble(String name, double value) {
		parameters.setDouble(name, value);
	}

    @Override
	public void setFloat(String name, float value) {
		parameters.setFloat(name, value);
	}

    @Override
	public int getInt(String name, int def) {
		return parameters.getInt(name, def);
	}

    @Override
	public void setBoolean(String name, boolean value) {
		parameters.setBoolean(name, value);
	}

    @Override
	public long getLong(String name, long def) {
		return parameters.getLong(name, def);
	}

    @Override
	public void setCalendar(String name, Calendar value) {
		parameters.setCalendar(name, value);
	}

    @Override
	public void setDate(String name, Date value) {
		parameters.setDate(name, value);
	}

    @Override
	public float getFloat(String name, float def) {
		return parameters.getFloat(name, def);
	}

    @Override
	public void setNumber(String name, Number value) {
		parameters.setNumber(name, value);
	}

    @Override
	public void removeProperty(String key) {
		parameters.removeProperty(key);
	}

    @Override
	public boolean isEditable() {
		return parameters.isEditable();
	}

    @Override
	public double getDouble(String name, double def) {
		return parameters.getDouble(name, def);
	}

    @Override
	public void clear() {
		parameters.clear();
	}

    @Override
	public String getFormatted(String name, String def, Object... values) {
		return parameters.getFormatted(name, def, values);
	}

    @Override
	public Iterator<Entry<String, Object>> iterator() {
		return parameters.iterator();
	}

    @Override
	public Calendar getCalendar(String name) throws MException {
		return parameters.getCalendar(name);
	}

    @Override
	public void forEach(Consumer<? super Entry<String, Object>> action) {
		parameters.forEach(action);
	}

    @Override
	public Date getDate(String name) {
		return parameters.getDate(name);
	}

    @Override
	public Number getNumber(String name, Number def) {
		return parameters.getNumber(name, def);
	}

    @Override
	public boolean isProperty(String name) {
		return parameters.isProperty(name);
	}

    @Override
	public Set<String> keys() {
		return parameters.keys();
	}

    @Override
	public Object get(Object name) {
		return parameters.get(name);
	}

    @Override
	public Object getProperty(String name) {
		return parameters.getProperty(name);
	}

    @Override
	public boolean containsValue(Object value) {
		return parameters.containsValue(value);
	}

    @Override
	public boolean containsKey(Object key) {
		return parameters.containsKey(key);
	}

    @Override
	public Collection<Object> values() {
		return parameters.values();
	}

    @Override
	public Set<Entry<String, Object>> entrySet() {
		return parameters.entrySet();
	}

    @Override
	public Spliterator<Entry<String, Object>> spliterator() {
		return parameters.spliterator();
	}

    @Override
	public int size() {
		return parameters.size();
	}

    @Override
	public boolean isEmpty() {
		return parameters.isEmpty();
	}

    @Override
	public Object put(String key, Object value) {
		return parameters.put(key, value);
	}

    @Override
	public Object remove(Object key) {
		return parameters.remove(key);
	}

    @Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		parameters.putAll(m);
	}

    @Override
	public Set<String> keySet() {
		return parameters.keySet();
	}

    @Override
	public boolean equals(Object o) {
		return parameters.equals(o);
	}

    @Override
	public int hashCode() {
		return parameters.hashCode();
	}

    @Override
	public Object getOrDefault(Object key, Object defaultValue) {
		return parameters.getOrDefault(key, defaultValue);
	}

    @Override
	public void forEach(BiConsumer<? super String, ? super Object> action) {
		parameters.forEach(action);
	}

    @Override
	public void replaceAll(BiFunction<? super String, ? super Object, ? extends Object> function) {
		parameters.replaceAll(function);
	}

    @Override
	public Object putIfAbsent(String key, Object value) {
		return parameters.putIfAbsent(key, value);
	}

    @Override
	public boolean remove(Object key, Object value) {
		return parameters.remove(key, value);
	}

    @Override
	public boolean replace(String key, Object oldValue, Object newValue) {
		return parameters.replace(key, oldValue, newValue);
	}

    @Override
	public Object replace(String key, Object value) {
		return parameters.replace(key, value);
	}

    @Override
	public Object computeIfAbsent(String key, Function<? super String, ? extends Object> mappingFunction) {
		return parameters.computeIfAbsent(key, mappingFunction);
	}

    @Override
	public Object computeIfPresent(String key,
			BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return parameters.computeIfPresent(key, remappingFunction);
	}

    @Override
	public Object compute(String key, BiFunction<? super String, ? super Object, ? extends Object> remappingFunction) {
		return parameters.compute(key, remappingFunction);
	}

    @Override
	public Object merge(String key, Object value,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		return parameters.merge(key, value, remappingFunction);
	}

}
