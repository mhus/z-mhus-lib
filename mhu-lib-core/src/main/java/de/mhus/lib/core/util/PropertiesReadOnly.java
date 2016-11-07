package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Set;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.NotSupportedException;

public class PropertiesReadOnly extends AbstractProperties {

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
	public void removeProperty(String key) {
		throw new NotSupportedException();
	}

	@Override
	public void setProperty(String key, Object value) {
		throw new NotSupportedException();
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public Set<String> keys() {
		return parent.keys();
	}

	@Override
	public int size() {
		return parent.size();
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
	public void clear() {
	}

}
