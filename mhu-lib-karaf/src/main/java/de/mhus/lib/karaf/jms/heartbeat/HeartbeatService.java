package de.mhus.lib.karaf.jms.heartbeat;

import javax.jms.JMSException;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.system.DefaultBase;
import de.mhus.lib.jms.heartbeat.Heartbeat;
import de.mhus.lib.jms.heartbeat.HeartbeatListener;
import de.mhus.lib.karaf.jms.JmsDataChannel;
import de.mhus.lib.karaf.jms.JmsDataChannelImpl;
import de.mhus.lib.karaf.jms.JmsManagerService;
import de.mhus.lib.karaf.jms.JmsUtil;

public class HeartbeatService extends JmsDataChannelImpl {

	private Heartbeat heartbeat;

	public HeartbeatService() {
		super("HeartbeatService","local", null);
		try {
			heartbeat = new Heartbeat();
			setChannel(heartbeat);
		} catch (JMSException e) {
			log().d(e);
		}
	}
	
	public void doActivate() {
		((DefaultBase)MSingleton.get().getBaseControl().base()).addObject(HeartbeatListener.class, new HeartbeatListener() {
			
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
	
				for (String cName : service.listChannels()) {
					try {
						log().d("heartbeat reset", cName);
						JmsDataChannel c = service.getChannel(cName);
						c.reset();
						if (c.getChannel() != null) {
							c.getChannel().reset();
							c.getChannel().open();
						}else
							log().w("channel is null",cName);
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

	protected void doTimerTask(String cmd) {
		if (getChannel().isClosed()) return;
		JmsManagerService service = JmsUtil.getService();
		if (service == null) return;
		try {
			service.doChannelBeat();
			heartbeat.sendHeartbeat(cmd);
		} catch (Throwable t) {
			log().t(t);
		}

	}

}
