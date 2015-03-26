package de.mhus.lib.core.jms;

import javax.jms.JMSException;
import javax.jms.Session;

import de.mhus.lib.core.MLog;

public abstract class JmsObject extends MLog {

	private boolean closed = false;

	public abstract void open() throws JMSException;
	public abstract void reset();
	
	protected void setClosed() {
		closed = true;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public void close() {
		log().i("close");
		reset();
		setClosed();
	}

	public abstract Session getSession();

}
