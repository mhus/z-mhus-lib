package de.mhus.lib.jms;

public interface JmsObjectProxy {

	Class<?> getInterface();
	public <I> I getObject();
	
}
