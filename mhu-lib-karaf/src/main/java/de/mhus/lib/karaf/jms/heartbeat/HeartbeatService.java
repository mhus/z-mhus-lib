package de.mhus.lib.karaf.jms.heartbeat;

import javax.jms.JMSException;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.lib.core.MTimer;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.jms.heartbeat.Heartbeat;
import de.mhus.lib.jms.heartbeat.HeartbeatReceiver;
import de.mhus.lib.jms.heartbeat.HeartbeatSender;
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
		try {
			JmsManagerService service = JmsUtil.getService();
			if (service == null) return;
			service.doChannelBeat();
			heartbeat.sendHeartbeat();
		} catch (Throwable t) {
			log().t(t);
		}
	}

}
