package de.mhus.lib.core.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import de.mhus.lib.core.MLog;

public abstract class JmsDestination extends MLog {

	protected JmsConnection con;

	public abstract Destination getDestination() throws JMSException;
	public abstract void open() throws JMSException;
	public abstract void close();
	
	public JmsConnection getConnection() {
		return con;
	}

	public Session getSession() {
		return con.getSession();
	}

}
