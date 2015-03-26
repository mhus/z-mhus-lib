package de.mhus.lib.core.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Topic;

public class JmsDestinationTopic extends JmsDestination {

	private String name;
	private Topic destination;

	public JmsDestinationTopic(JmsConnection con, String name) {
		this.con = con;
		this.name = name;
	}

	@Override
	public synchronized void open() throws JMSException {
		if (destination == null || getSession() == null) {
			con.open();
			log().i("destination",name);
            destination = getSession().createTopic(name);
		}
	}
	
	@Override
	public Destination getDestination() throws JMSException {
		open();
		return destination;
	}
	
	@Override
	public void close() {
		destination = null;
	}

	@Override
	public String toString() {
		return "topic/" + name;
	}
}
