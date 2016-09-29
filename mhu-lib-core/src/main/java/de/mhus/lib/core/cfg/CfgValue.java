package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MSystem;

/**
 * <p>Abstract CfgValue class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public abstract class CfgValue<T> {

	private String path;
	private T def;
	private T value;
	private Object owner;
	
	/**
	 * <p>Constructor for CfgValue.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a T object.
	 */
	public CfgValue(Object owner, String path, T def) {
		this.owner = owner;
		this.path = path;
		this.def = def;
		MSingleton.getCfgUpdater().register(this);
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
		onPreUpdate(newValue);
		this.value = newValue;
		onPostUpdate(value);
	}

	/**
	 * <p>loadValue.</p>
	 *
	 * @return a T object.
	 */
	protected abstract T loadValue();
	
	/**
	 * <p>onPreUpdate.</p>
	 *
	 * @param newValue a T object.
	 */
	protected void onPreUpdate(T newValue) {
		
	}
	
	/**
	 * <p>onPostUpdate.</p>
	 *
	 * @param newValue a T object.
	 */
	protected void onPostUpdate(T newValue) {
		
	}

	/**
	 * <p>isOwner.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isOwner(String name) {
		return MSingleton.get().getCfgManager().isOwner(owner, name);
	}
	
}
