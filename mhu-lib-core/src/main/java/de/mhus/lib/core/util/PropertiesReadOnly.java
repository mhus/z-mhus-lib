package de.mhus.lib.core.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import de.mhus.lib.basics.ReadOnly;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.IReadProperties;
import de.mhus.lib.errors.MException;

public class PropertiesReadOnly implements IReadProperties, ReadOnly {

	private static final long serialVersionUID = 1L;
	private IProperties parent;

	public PropertiesReadOnly(IProperties parent) {
		this.parent = parent;
	}
	
	@Override
	public Object get(Object name) {
		return parent.get(name);
	}

	@Override
	public boolean isProperty(String name) {
		return parent.isProperty(name);
	}

	@Override
	public Set<String> keys() {
		return parent.keys();
	}

	@Override
	public Object getProperty(String name) {
		return get(name);
	}

	@Override
	public boolean containsValue(Object value) {
		return parent.containsValue(value);
	}

	@Override
	public Collection<Object> values() {
		return parent.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return parent.entrySet();
	}

	@Override
	public String getString(String name, String def) {
		return parent.getString(name, def);
	}

	@Override
	public String getString(String name) throws MException {
		return parent.getString(name);
	}

	@Override
	public boolean getBoolean(String name, boolean def) {
		return parent.getBoolean(name,def);
	}

	@Override
	public boolean getBoolean(String name) throws MException {
		return parent.getBoolean(name);
	}

	@Override
	public int getInt(String name, int def) {
		return parent.getInt(name, def);
	}

	@Override
	public long getLong(String name, long def) {
		return parent.getLong(name, def);
	}

	@Override
	public float getFloat(String name, float def) {
		return parent.getFloat(name, def);
	}

	@Override
	public double getDouble(String name, double def) {
		return parent.getDouble(name, def);
	}

	@Override
	public Calendar getCalendar(String name) throws MException {
		return parent.getCalendar(name);
	}

	@Override
	public Date getDate(String name) {
		return parent.getDate(name);
	}

	@Override
	public Number getNumber(String name, Number def) {
		return parent.getNumber(name, def);
	}

}
