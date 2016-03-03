package de.mhus.lib.core.configupdater;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;

/**
 * <p>Abstract ConfigValue class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public abstract class ConfigValue<T> {

	private String path;
	private T def;
	private T value;
	private Object owner;
	
	/**
	 * <p>Constructor for ConfigValue.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a T object.
	 */
	public ConfigValue(Object owner, String path, T def) {
		this.owner = owner;
		this.path = path;
		this.def = def;
		MSingleton.getConfigUpdater().register(this);
		update();
	}
	
	/**
	 * <p>value.</p>
	 *
	 * @return a T object.
	 */
	public T value() {
		return value == null ? def : value;
	}
	
	/**
	 * <p>Getter for the field <code>path</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * <p>Getter for the field <code>owner</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getOwner() {
		return owner;
	}
	
	/**
	 * <p>getDefault.</p>
	 *
	 * @return a T object.
	 */
	public T getDefault() {
		return def;
	}
	
	void update() {
		T newValue = loadValue();
		if (MSystem.equals(value, newValue)) return;
		onUpdate(newValue);
		this.value = newValue;
	}

	/**
	 * <p>loadValue.</p>
	 *
	 * @return a T object.
	 */
	protected abstract T loadValue();
	
	/**
	 * <p>onUpdate.</p>
	 *
	 * @param newValue a T object.
	 */
	protected void onUpdate(T newValue) {
		
	}
	
}
