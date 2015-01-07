package de.mhus.lib.core;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Properties;
import java.util.Set;

import de.mhus.lib.core.util.SetCast;

public class MProperties extends IProperties implements Externalizable {

	protected Properties properties = null;
	
	public MProperties() {
		this(new Properties());
	}
	
	public MProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Object getProperty(String name) {
		return properties.get(name);
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
	public void setProperty(String key, Object value) {
		if (value == null)
			properties.remove(key);
		else
			properties.put(key, value );
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public Set<String> keys() {
		return new SetCast<Object, String>(properties.keySet());
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, properties);
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject( properties);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		properties = (Properties) in.readObject();
	}
	
	
}
