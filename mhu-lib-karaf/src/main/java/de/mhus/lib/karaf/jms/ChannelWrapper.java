package de.mhus.lib.karaf.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import de.mhus.lib.jms.JmsDestination;
import de.mhus.lib.jms.ServerJms;

public class ChannelWrapper extends ServerJms {

	private JmsDataChannelImpl forward;

	public ChannelWrapper(JmsDataChannelImpl channel) {
		super(new JmsDestination(channel.getDestination(),  channel.isDestinationTopic()) );
		this.forward = channel;
	}

	@Override
	public void receivedOneWay(Message msg) throws JMSException {
		forward.receivedOneWay(msg);
	}

	@Override
	public Message received(Message msg) throws JMSException {
		return forward.received(msg);
	}

	public String getType() {
		return forward == null ? "" : forward.getClass().getCanonicalName();
	}


}
