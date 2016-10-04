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
			sender = new HeartbeatSender(getDestination().getConnection());
		}
		sender.open();
	}

	@Override
	public void reset() {
		super.reset();
		if (sender != null) {
			sender.getDestination().setConnection(getDestination().getConnection());
			sender.reset();
		}
	}

	@Override
	public void close() {
		super.close();
		if (sender != null) sender.close();
	}

	@Override
	public void reopen() {
		super.reopen();
		if (sender != null) sender.reopen();
	}

	public void sendHeartbeat(String cmd) {
		if (sender != null)
			sender.sendHeartbeat(cmd);
	}

}
