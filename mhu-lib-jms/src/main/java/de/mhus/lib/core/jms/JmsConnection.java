package de.mhus.lib.core.jms;

import java.util.WeakHashMap;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsConnection extends JmsObject implements ExceptionListener {

    private static final Boolean NON_TRANSACTED = false;

	private Connection connection;
	private ActiveMQConnectionFactory connectionFactory;
	private Session session;
	private WeakHashMap<JmsBase, JmsBase> baseRegistry = new WeakHashMap<>();

	private String url;

	public JmsConnection(String url, String user, String password) throws JMSException {
		connectionFactory = new ActiveMQConnectionFactory(user, password, url);
		this.url = url;
	}

	@Override
	public synchronized void open() throws JMSException {
		if (isClosed()) throw new JMSException("connection closed");
		if (connection == null) {
			log().i("connect",url);
	        Connection con = connectionFactory.createConnection();
	        con.start();
	        connection = con;
            connection.setExceptionListener(this);
            session = connection.createSession(NON_TRANSACTED, Session.AUTO_ACKNOWLEDGE);
            
            doBaseBeat();
		}
	}
	
	public JmsDestination createTopic(String name) throws JMSException {
        return new JmsDestinationTopic(this,name);
	}

	public JmsDestination createQueue(String name) throws JMSException {
        return new JmsDestinationQueue(this,name);
	}
	
	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public void reset() {
		log().i("reset",connection);
		try {
			session.close();
		} catch (Throwable t) {log().t(t);}
		try {
			connection.close();
		} catch (Throwable t) {log().t(t);}
		connection = null;
		session = null;
	}
		
	@Override
	public void onException(JMSException exception) {
		log().w("kill connection",connection,exception);
		try {
			session.close();
		} catch (Throwable t) {log().t(t);}
		try {
			connection.close();
		} catch (Throwable t) {log().t(t);}
		connection = null;
		session = null;
	}
	
	public void registerBase(JmsBase base) {
		synchronized (baseRegistry) {
			baseRegistry.put(base, base);
		}
	}
	
	public void unregisterBase(JmsBase base) {
		synchronized (baseRegistry) {
			baseRegistry.remove(base);
		}
	}
	
	public void doBaseBeat() {
		synchronized (baseRegistry) {
			for (JmsBase base : baseRegistry.keySet())
				try {
					base.doBeat();
				} catch (Throwable t) {
					log().t("beat",base,t);
				}
		}
	}
	
}
