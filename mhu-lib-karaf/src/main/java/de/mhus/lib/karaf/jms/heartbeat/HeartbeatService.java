package de.mhus.lib.karaf.jms.heartbeat;

import javax.jms.JMSException;

import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.heartbeat.Heartbeat;
import de.mhus.lib.karaf.jms.AbstractJmsDataChannel;

public class HeartbeatService extends AbstractJmsDataChannel {

/*	
	public void doActivate() {
		((DefaultBase)MApi.get().getBaseControl().base()).addObject(HeartbeatListener.class, new HeartbeatListener() {
			
			@Override
			public void heartbeatReceived(String txt) {
				if (txt == null) return;
				if (txt.startsWith("reset,")) {
					doChanelReset();
				} else
				if (txt.startsWith("reset-all,")) {
					doAllChanelReset();
				}
			}
		});
	}
	

	protected void doChanelReset() {
		if (getChannel().isClosed()) return;
		MThread.asynchron(new Runnable() {
			
			@Override
			public void run() {
				MThread.sleep(1000);
				getChannel().reset();
			}
		});
	}

	protected void doAllChanelReset() {
		final JmsManagerService service = JmsUtil.getService();
		if (service == null) return;
		MThread.asynchron(new Runnable() {
			
			@Override
			public void run() {
				MThread.sleep(1000);
	
				for (JmsDataChannel c : service.getChannels()) {
					try {
						log().d("heartbeat reset", c);
						c.reset();
						if (c.getChannel() != null) {
							c.getChannel().reset();
							c.getChannel().open();
						}else
							log().w("channel is null",c);
					} catch (Throwable t) {
						log().w(t);
					}
				}
			}
		});
	}
	
	public void doDeactivate() {
		getChannel().close();
	}
 */

	protected void doTimerTask(String cmd) {
		JmsChannel c = getChannel();
		if (c == null || c.isClosed()) return;
		try {
			((Heartbeat)c).sendHeartbeat(cmd);
		} catch (Throwable t) {
			log().t(t);
		}

	}
	public void setName(String name) {
		this.name = name;
	}


	public void setConnectionName(String conName) {
		this.connectionName = conName;
	}


	@Override
	public JmsChannel createChannel() throws JMSException {
		return new Heartbeat();
	}

}
