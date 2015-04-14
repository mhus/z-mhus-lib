package de.mhus.lib.jms.heartbeat;

import java.util.LinkedList;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.jms.ClientJms;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.JmsDestination;

public class HeartbeatSender extends ClientJms {

	public static final String TOPIC_NAME = "public.heartbeat";
	
	public HeartbeatSender(JmsConnection con) throws JMSException {
		super(con == null ? new JmsDestination(TOPIC_NAME, true) : con.createTopic(TOPIC_NAME));
	}
	public HeartbeatSender() throws JMSException {
		this(null);
	}
	
	public void sendHeartbeat() {
		if (getSession() == null) {
			log().i("heartbeat has no session");
			return;
		}
		try {
			TextMessage msg = getSession().createTextMessage(MSystem.getAppIdent());
			Message[] ret = sendJmsBroadcast(msg);
			LinkedList<String> hosts = new LinkedList<>();
			for (Message m : ret) {
				if (m instanceof TextMessage)
					hosts.add( ((TextMessage)m).getText() );
			}
			log().i("hosts",hosts);
		} catch (JMSException e) {
			log().w(e);
		}
	}
}
