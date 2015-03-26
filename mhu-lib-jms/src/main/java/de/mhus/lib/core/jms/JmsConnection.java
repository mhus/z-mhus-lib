package de.mhus.lib.core.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import de.mhus.lib.core.MLog;

public class JmsConnection extends MLog implements ExceptionListener {

    private static final Boolean NON_TRANSACTED = false;

	private Connection connection;
	private ActiveMQConnectionFactory connectionFactory;
	private Session session;

	private String url;

	public JmsConnection(String url, String user, String password) throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(user, password, url);
		this.url = url;
	}

	public synchronized void open() throws JMSException {
		if (connection == null) {
			log().i("connect",url);
	        Connection con = connectionFactory.createConnection();
	        con.start();
	        connection = con;
            connection.setExceptionListener(this);
            session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
		}
	}
	
	public JmsDestination createTopic(String name) throws JMSException {
        return new JmsDestinationTopic(this,name);
	}

	public JmsDestination createQueue(String name) throws JMSException {
        return new JmsDestinationQueue(this,name);
	}
	
	public Session getSession() {
		return session;
	}

	public void close() {
		log().i("close",connection);
		try {
			session.close();
		} catch (Throwable t) {}
		try {
			connection.close();
		} catch (Throwable t) {}
		connection = null;
		session = null;
	}
	
	@Override
	public void onException(JMSException exception) {
		log().w("kill connection",connection,exception);
		try {
			session.close();
		} catch (Throwable t) {}
		try {
			connection.close();
		} catch (Throwable t) {}
		connection = null;
		session = null;
	}
	
}
