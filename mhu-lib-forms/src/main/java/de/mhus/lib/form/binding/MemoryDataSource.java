package de.mhus.lib.form.binding;

import java.util.Properties;
import java.util.Set;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.util.SetCast;
import de.mhus.lib.form.DataSource;

public class MemoryDataSource extends DataSource {

	protected Properties properties = null;


	public MemoryDataSource() {
		this(new Properties());
	}
	
	public MemoryDataSource(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Object getProperty(String name) {
		return properties.getProperty(name);
	}

	@Override
	public boolean isPropertyPossible(String name) {
		return true;
	}
	
	@Override
	public boolean isProperty(String name) {
		return properties.containsKey(name);
	}

	@Override
	public void removeProperty(String key) {
		properties.remove(key);
	}

	@Override
	public void setPropertyData(String key, Object value) {
		properties.setProperty(key, MCast.objectToString(value) );
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public Set<String> keys() {
		return new SetCast<Object, String>(properties.keySet());
	}
	
	public Properties getMemory() {
		return properties;
	}

}
