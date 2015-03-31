package de.mhus.lib.jms;

import java.util.UUID;

import javax.jms.Session;

public abstract class JmsChannel extends JmsObject {

	protected JmsDestination dest;

	public JmsChannel(String destination, boolean destinationTopic) {
		dest = new JmsDestination(destination, destinationTopic);
	}
	
	public JmsChannel(JmsDestination dest) {
		this.dest = dest;
	}

	protected String createMessageId() {
		return UUID.randomUUID().toString();
	}

	public JmsDestination getDestination() {
		return dest;
	}

	@Override
	public Session getSession() {
		return dest.getSession();
	}
	
	@Override
	public void close() {
		try {
			dest.getConnection().unregisterChannel(this);
		} catch (Throwable t) {log().t(t);}
		super.close();
	}

	@Override
	public String toString() {
		return getName() + "/" + getClass().getSimpleName();
	}
	
	public abstract void doBeat();
	public abstract String getName();

	public void checkConnection() {
	}

}
