package de.mhus.lib.jms.heartbeat;

import javax.jms.JMSException;

import de.mhus.lib.jms.JmsConnection;

public class Heartbeat extends HeartbeatReceiver {

	protected HeartbeatSender sender;

	public Heartbeat() throws JMSException {
		super();
	}

	public Heartbeat(JmsConnection con) throws JMSException {
		super(con);
	}

	@Override
	public synchronized void open() throws JMSException {
		super.open();
		if (sender == null) {
			sender = new HeartbeatSender(getJmsDestination().getConnection());
		}
		sender.open();
	}

	@Override
	public void reset() {
		super.reset();
		if (sender != null) {
			sender.getJmsDestination().setConnection(getJmsDestination().getConnection());
			sender.reset();
		}
	}

	@Override
	public void close() {
		super.close();
		if (sender != null) sender.close();
	}

	@Override
	public void reopen() throws JMSException {
		super.reopen();
		if (sender != null) sender.reopen();
	}

	public void sendHeartbeat(String cmd) {
		if (sender != null) {
			if (sender.isClosed() || !sender.isConnected()) {
				sender = null;
				closed = false;
				try {
					open();
				} catch (JMSException e) {
					log().d(getName(),e);
				}
			}
			sender.sendHeartbeat(cmd);
		}
	}

}
