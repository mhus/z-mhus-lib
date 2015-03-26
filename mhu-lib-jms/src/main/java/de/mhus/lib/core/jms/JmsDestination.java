package de.mhus.lib.core.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

public abstract class JmsDestination extends JmsObject {

	protected JmsConnection con;

	public abstract Destination getDestination() throws JMSException;
	
	public JmsConnection getConnection() {
		return con;
	}

	@Override
	public Session getSession() {
		return con.getSession();
	}

}
