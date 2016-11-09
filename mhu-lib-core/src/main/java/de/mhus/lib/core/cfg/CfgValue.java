package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;

public abstract class CfgValue<T> {

	private String path;
	private T def;
	private T value;
	private Object owner;
	
	public CfgValue(Object owner, String path, T def) {
		this.owner = owner;
		this.path = path;
		this.def = def;
		MSingleton.getCfgUpdater().register(this);
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
	
	public Class<?> getOwnerClass() {
		if (owner == null) return null;
		if (owner instanceof Class<?>) return (Class<?>)owner;
		return owner.getClass();
	}
	
	public T getDefault() {
		return def;
	}
	
	void update() {
		T newValue = loadValue();
		if (MSystem.equals(value, newValue)) return;
		onPreUpdate(newValue);
		this.value = newValue;
		onPostUpdate(value);
	}

	protected abstract T loadValue();
	
	protected abstract T loadValue(String value);
	
	protected void onPreUpdate(T newValue) {
		
	}
	
	protected void onPostUpdate(T newValue) {
		
	}

	public boolean isOwner(String name) {
		return MSingleton.get().getCfgManager().isOwner(owner, name);
	}

	public void setValue(String v) {
		T newValue = loadValue(v);
		if (newValue == null) return;
		if (MSystem.equals(value, newValue)) return;
		onPreUpdate(newValue);
		this.value = newValue;
		onPostUpdate(value);
	}
	
}
