package de.mhus.lib.jms.heartbeat;

import javax.jms.JMSException;

import de.mhus.lib.core.MTimer;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.jms.JmsConnection;

public class HeartbeatTimer extends Heartbeat {

	private MTimer timer;

	public HeartbeatTimer() throws JMSException {
		super();
	}

	public HeartbeatTimer(JmsConnection con) throws JMSException {
		super(con);
	}

	@Override
	public synchronized void open() throws JMSException {
		super.open();
		if (timer == null) {
			timer = new MTimer(true);
			timer.schedule(new MTimerTask() {
				
				@Override
				public void doit() throws Exception {
					doTimerTask();
				}
			}, 10000,60000 * 5);
		}
	}

	protected void doTimerTask() {
		if (isClosed() || sender == null || sender.isClosed()) return;
		sender.sendHeartbeat(null);
	}

	@Override
	public void close() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		super.close();
	}

	
	
}
