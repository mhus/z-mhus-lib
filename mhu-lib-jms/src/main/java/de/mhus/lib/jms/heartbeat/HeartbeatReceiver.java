package de.mhus.lib.jms.heartbeat;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.JmsDestination;
import de.mhus.lib.jms.ServerJms;

public class HeartbeatReceiver extends ServerJms {

	public HeartbeatReceiver(JmsConnection con) throws JMSException {
		super(con == null ? new JmsDestination(HeartbeatSender.TOPIC_NAME, true) : con.createTopic(HeartbeatSender.TOPIC_NAME));
	}
	
	public HeartbeatReceiver() throws JMSException {
		this(null);
	}

	@Override
	public void receivedOneWay(Message msg) throws JMSException {
		
	}

	@Override
	public Message received(Message msg) throws JMSException {
		String txt = "";
		if (msg instanceof TextMessage) txt =((TextMessage)msg).getText();
		log().d("received",txt);
		HeartbeatListener listener = MApi.lookup(HeartbeatListener.class);
		if (listener != null)
			listener.heartbeatReceived(txt);
		TextMessage ret = getSession().createTextMessage(MSystem.getAppIdent());
		return ret;
	}

}
