package de.mhus.lib.karaf.jms;

import de.mhus.lib.jms.JmsChannel;

public interface JmsDataChannel {

	void reset();
	JmsChannel getChannel();
	String getName();
	String getConnectionName();
	Class<?> getInterface();
	<I> I getObject(Class<? extends I> ifc);
	
}
