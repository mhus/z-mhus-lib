package de.mhus.lib.core.util;

import java.util.Set;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.errors.NotSupportedException;

public class PropertiesReadOnly extends AbstractProperties {

	private AbstractProperties parent;

	public PropertiesReadOnly(AbstractProperties parent) {
		this.parent = parent;
	}
	
	@Override
	public Object getProperty(String name) {
		return parent.getProperty(name);
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

}
