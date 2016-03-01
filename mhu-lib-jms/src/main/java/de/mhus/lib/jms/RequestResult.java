package de.mhus.lib.jms;

import de.mhus.lib.core.AbstractProperties;

public class RequestResult<T> {

	public RequestResult(T result, AbstractProperties properies) {
		this.result = result;
		this.properties = properies;
	}
	
	private T result;
	private AbstractProperties properties;
	
	public T getResult() {
		return result;
	}
	
	public AbstractProperties getProperties() {
		return properties;
	}
	
	@Override
	public String toString() {
		return "Result[" + properties + "," + result + "]";
	}
	
}
