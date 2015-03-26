package de.mhus.lib.core.jms;

import de.mhus.lib.core.IProperties;

public class RequestResult<T> {

	public RequestResult(T result, IProperties properies) {
		this.result = result;
		this.properties = properies;
	}
	
	private T result;
	private IProperties properties;
	
	public T getResult() {
		return result;
	}
	
	public IProperties getProperties() {
		return properties;
	}
	
	@Override
	public String toString() {
		return "Result[" + properties + "," + result + "]";
	}
	
}
