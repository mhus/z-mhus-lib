package de.mhus.lib.karaf.jms;

import de.mhus.lib.jms.JmsChannel;

public interface JmsDataChannel {

	void reset();
	void reset(JmsManagerService service);
	JmsChannel getChannel();
	String getName();
	String getConnectionName();
	@Deprecated
	Class<?> getInterface();
	@Deprecated
	<I> I getObject(Class<? extends I> ifc);
	String getInformation();
	
}
