package de.mhus.lib.jms;

public interface JmsChannelService {

	Class<?> getInterface();
	public <I> I getObject(Class<? extends I> ifc);
	
}
