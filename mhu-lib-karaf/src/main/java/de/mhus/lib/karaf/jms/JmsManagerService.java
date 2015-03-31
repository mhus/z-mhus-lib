package de.mhus.lib.karaf.jms;

import javax.jms.JMSException;

import de.mhus.lib.jms.JmsConnection;

public interface JmsManagerService {
	
	void addConnection(String name, JmsConnection con);
	void addConnection(String name, String url, String user, String password) throws JMSException;
	String[] listConnections();
	JmsConnection getConnection(String name);
	void removeConnection(String name);
	String[] listChannels();
	JmsDataChannel getChannel(String name);
	void addChannel(JmsDataChannel channel);
	void removeChannel(String name);
	<I> I getObjectForInterface(Class<? extends I> ifc);
	void resetChannels();

}
