package de.mhus.lib.karaf.jms.heartbeat;

import java.util.HashMap;
import java.util.HashSet;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.core.util.TimerFactory;
import de.mhus.lib.core.util.TimerIfc;
import de.mhus.lib.karaf.jms.JmsManagerService;
import de.mhus.lib.karaf.jms.JmsUtil;

@Component(provide=HeartbeatAdmin.class,immediate=true,name="de.mhus.lib.karaf.jms.heartbeat.HeartbeatAdmin")
public class HeartbeatAdminImpl extends MLog implements HeartbeatAdmin {

	private TimerIfc timer;
	private HashMap<String, HeartbeatService> services = new HashMap<>();
	private boolean enabled = true;
	private MTimerTask timerTask;

	@Activate
	public void doActivate(ComponentContext ctx) {
	}
	
	protected void doTimerTask() {
		if (!enabled) return;
		sendHeartbeat();
	}

	@Reference(service=TimerFactory.class)
	public void setTimerFactory(TimerFactory factory) {
		log().i("create timer");
		timer = factory.getTimer();
		timerTask = new MTimerTask() {
			
			@Override
			public void doit() throws Exception {
				doTimerTask();
			}
		};
		timer.schedule(timerTask, 10000, 60000 * 5);
	}
	
	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		timerTask.cancel();
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
