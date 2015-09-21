package de.mhus.lib.jms;

import java.io.Serializable;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

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
		log().d("close");
		reset();
		setClosed();
	}

	public abstract Session getSession();

	public abstract boolean isConnected();
	
	public void reopen() {
		closed = false;
		reset();
	}

	public BytesMessage createBytesMessage() throws JMSException {
		if (getSession() == null) throw new JMSException("not connected");
		return getSession().createBytesMessage();
	}
	public MapMessage createMapMessage() throws JMSException {
		if (getSession() == null) throw new JMSException("not connected");
		return getSession().createMapMessage();
	}
	public Message createMessage() throws JMSException {
		if (getSession() == null) throw new JMSException("not connected");
		return getSession().createMessage();
	}
	public ObjectMessage createObjectMessage() throws JMSException {
		if (getSession() == null) throw new JMSException("not connected");
		return getSession().createObjectMessage();
	}
	public ObjectMessage createObjectMessage(Serializable arg0)
			throws JMSException {
		if (getSession() == null) throw new JMSException("not connected");
		return getSession().createObjectMessage(arg0);
	}
	public StreamMessage createStreamMessage() throws JMSException {
		if (getSession() == null) throw new JMSException("not connected");
		return getSession().createStreamMessage();
	}
	public TextMessage createTextMessage() throws JMSException {
		if (getSession() == null) throw new JMSException("not connected");
		return getSession().createTextMessage();
	}
	public TextMessage createTextMessage(String arg0) throws JMSException {
		if (getSession() == null) throw new JMSException("not connected");
		return getSession().createTextMessage(arg0);
	}
	
}
