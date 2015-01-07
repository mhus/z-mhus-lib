package de.mhus.lib.core.util;

public class ObjectContainer <T> {

	protected T object;

	public ObjectContainer() {}
	
	public ObjectContainer(T in) {
		object = in;
	}
	
	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}
}
