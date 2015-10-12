package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;

public abstract class ConfigValue<T> {

	private String path;
	private T def;
	private T value;
	private Object owner;
	
	public ConfigValue(Object owner, String path, T def) {
		this.owner = owner;
		this.path = path;
		this.def = def;
		MSingleton.getConfigUpdater().register(this);
		update();
	}
	
	public T value() {
		return value == null ? def : value;
	}
	
	public String getPath() {
		return path;
	}
	
	public Object getOwner() {
		return owner;
	}
	
	public T getDefault() {
		return def;
	}
	
	void update() {
		T newValue = loadValue();
		if (MSystem.equals(value, newValue)) return;
		onUpdate(newValue);
		this.value = newValue;
	}

	protected abstract T loadValue();
	
	protected void onUpdate(T newValue) {
		
	}
	
}
