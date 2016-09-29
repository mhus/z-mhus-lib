package de.mhus.lib.core.util;

/**
 * <p>ObjectContainer class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ObjectContainer <T> {

	protected T object;

	/**
	 * <p>Constructor for ObjectContainer.</p>
	 */
	public ObjectContainer() {}
	
	/**
	 * <p>Constructor for ObjectContainer.</p>
	 *
	 * @param in a T object.
	 */
	public ObjectContainer(T in) {
		object = in;
	}
	
	/**
	 * <p>Getter for the field <code>object</code>.</p>
	 *
	 * @return a T object.
	 */
	public T getObject() {
		return object;
	}

	/**
	 * <p>Setter for the field <code>object</code>.</p>
	 *
	 * @param object a T object.
	 */
	public void setObject(T object) {
		this.object = object;
	}
}
