package de.mhus.lib.core.jms;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Session;

import de.mhus.lib.core.MLog;

public abstract class JmsBase extends MLog {

	protected JmsDestination dest;

	public JmsBase(JmsDestination dest) {
		this.dest = dest;
	}

	public abstract void open() throws JMSException;
	public abstract void close();

	protected String createMessageId() {
		return UUID.randomUUID().toString();
	}

	public JmsDestination getDestination() {
		return dest;
	}

	public Session getSession() {
		return dest.getSession();
	}

}
