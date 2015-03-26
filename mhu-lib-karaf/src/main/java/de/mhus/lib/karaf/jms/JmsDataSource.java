package de.mhus.lib.karaf.jms;

import javax.jms.JMSException;

import de.mhus.lib.jms.JmsConnection;

public interface JmsDataSource {

	String getName();

	JmsConnection createConnection() throws JMSException;
	
}
