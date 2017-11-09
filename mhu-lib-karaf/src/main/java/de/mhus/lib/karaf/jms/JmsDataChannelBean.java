package de.mhus.lib.karaf.jms;

import javax.jms.JMSException;

import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsDestination;

public class JmsDataChannelBean extends AbstractJmsDataChannel {

	private JmsChannel channel;
	private String destination;
	private boolean destinationTopic;
	
	public JmsDataChannelBean() {}
	
	public JmsDataChannelBean(String destination, String connectionName, JmsChannel channel) {
		name = connectionName + "/" + destination;
		this.destination = destination;
		this.connectionName = connectionName;
		setChannel(channel);
	}
	
	public JmsDataChannelBean(String destination, String connectionName, boolean destinationTopic, JmsChannel channel) {
		name = connectionName + "/" + destination;
		this.destination = destination;
		this.destinationTopic = destinationTopic;
		this.connectionName = connectionName;
		setChannel(channel);
	}
	
	@Override
	protected JmsChannel createChannel() throws JMSException {
		JmsDestination dest = new JmsDestination(destination, destinationTopic);
		channel.reset(dest);
		return channel;
	}

	public void setChannel(JmsChannel channel) {
		this.channel = channel;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public boolean isDestinationTopic() {
		return destinationTopic;
	}

	public void setDestinationTopic(boolean destinationTopic) {
		this.destinationTopic = destinationTopic;
	}

}
