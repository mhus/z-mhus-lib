package de.mhus.lib.karaf.jms.heartbeat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MTimer;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.karaf.jms.JmsDataChannel;
import de.mhus.lib.karaf.jms.JmsManagerService;
import de.mhus.lib.karaf.jms.JmsUtil;

@Component(provide=HeartbeatAdmin.class,immediate=true,name="de.mhus.lib.karaf.jms.heartbeat.HeartbeatAdmin")
public class HeartbeatAdminImpl extends MLog implements HeartbeatAdmin {

	private MTimer timer;
	private HashMap<String, HeartbeatService> services = new HashMap<>();
	private boolean enabled = true;

	@Activate
	public void doActivate(ComponentContext ctx) {
		timer = new MTimer(true);
		timer.schedule(new MTimerTask() {
			
			@Override
			public void doit() throws Exception {
				doTimerTask();
			}
		}, 10000, 60000 * 5);
	}
	
	protected void doTimerTask() {
		if (!enabled) return;
		sendHeartbeat();
	}

	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		timer.cancel();
	}

	@Override
	public void sendHeartbeat() {
		synchronized (services) {
			JmsManagerService jmsService = JmsUtil.getService();
			if (jmsService == null) return;
			String[] conList = jmsService.listConnections();
			HashSet<String> existList = new HashSet<>();
			existList.addAll(services.keySet());
			// work over all existing connections
			for (String conName : conList) {
				try {
					HeartbeatService service = services.get(conName);
					if (service == null) {
						service = new HeartbeatService();
						service.setName(service.getName() + ":" + conName);
						service.setConnectionName(conName);
						jmsService.addChannel(service);
						services.put(conName, service);
						service.doActivate();
					} else {
						if (!service.getChannel().isClosed())
							service.doTimerTask();
					}
				} catch (Throwable t) {
					log().d(conName,t);
				}
				existList.remove(conName);
			}
			// remove overlapping
			for (String conName : existList) {
				HeartbeatService service = services.get(conName);
				jmsService.removeChannel(service.getName());
				service.doDeactivate();
			}
		}
	}

	@Override
	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
