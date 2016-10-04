package de.mhus.lib.karaf.jms.heartbeat;

import javax.jms.JMSException;

import de.mhus.lib.jms.heartbeat.Heartbeat;
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
	}
	

	public void doDeactivate() {
		getChannel().close();
	}

	protected void doTimerTask() {
		if (getChannel().isClosed()) return;
		JmsManagerService service = JmsUtil.getService();
		if (service == null) return;
		try {
			service.doChannelBeat();
			heartbeat.sendHeartbeat();
		} catch (Throwable t) {
			log().t(t);
		}
		
		// send beat ...
		if (service == null) return;
		for (String cName : service.listChannels()) {
			try {
				System.out.println(cName);
				JmsDataChannel c = service.getChannel(cName);
				c.getChannel().doBeat();
				if (c.getChannel() != null) {
					c.getChannel().reset();
					c.getChannel().open();
				}else
					System.out.println("... channel is null");
			} catch (Throwable t) {
				System.out.println(t);
			}
		}
		
	}

}
