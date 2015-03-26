package de.mhus.lib.core.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;

public class JmsDestinationQueue extends JmsDestination {

	private String name;
	private Queue destination;

	public JmsDestinationQueue(JmsConnection con, String name) {
		this.con = con;
		this.name = name;
	}

	@Override
	public synchronized void open() throws JMSException {
		if (isClosed()) throw new JMSException("destination closed");
		if (destination == null || getSession() == null) {
			con.open();
			log().i("destination",name);
            destination = getSession().createQueue(name);
		}
	}
	
	@Override
	public Destination getDestination() throws JMSException {
		open();
		return destination;
	}

	@Override
	public void reset() {
		destination = null;
	}
	
	@Override
	public String toString() {
		return "queue/" + name;
	}
}
