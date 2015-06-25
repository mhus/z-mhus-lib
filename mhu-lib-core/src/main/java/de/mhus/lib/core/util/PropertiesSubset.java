package de.mhus.lib.core.util;

import java.util.HashSet;
import java.util.Set;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.errors.NotSupportedException;

public class PropertiesSubset extends IProperties {

	private IProperties parent;
	private String prefix;
	private boolean readonly;
	
	public PropertiesSubset(IProperties parent, String prefix) {
		this(parent,prefix,false);
	}
	
	public PropertiesSubset(IProperties parent, String prefix, boolean readonly) {
		this.parent = parent;
		this.prefix = prefix;
		this.readonly = readonly;
	}
	
	@Override
	public Object getProperty(String name) {
		return parent.getProperty(prefix + name);
	}

	@Override
	public boolean isProperty(String name) {
		return parent.isProperty(prefix + name);
	}

	@Override
	public void removeProperty(String key) {
		if (readonly)
			throw new NotSupportedException();
		parent.removeProperty(prefix + key);
	}

	@Override
	public void setProperty(String key, Object value) {
		if (readonly)
			throw new NotSupportedException();
		parent.setProperty(prefix + key, value);
	}

	@Override
	public boolean isEditable() {
		if (readonly) return false;
		return parent.isEditable();
	}

	@Override
	public Set<String> keys() {
		HashSet<String> out = new HashSet<>();
		for (String k : parent.keys())
			if (k.startsWith(prefix))
				out.add(k);
		return out;
	}

}
