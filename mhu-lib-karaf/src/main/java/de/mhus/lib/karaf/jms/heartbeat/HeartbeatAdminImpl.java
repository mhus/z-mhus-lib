package de.mhus.lib.karaf.jms.heartbeat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.core.base.service.TimerFactory;
import de.mhus.lib.core.base.service.TimerIfc;
import de.mhus.lib.karaf.MOsgi.Service;
import de.mhus.lib.karaf.jms.JmsDataChannel;
import de.mhus.lib.karaf.jms.JmsDataSource;
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
		sendHeartbeat(null);
	}
	
	@Override
	public void sendHeartbeat(String cmd) {
		synchronized (services) {
			JmsManagerService jmsService = JmsUtil.getService();
			if (jmsService == null) return;
			
			jmsService.doChannelBeat();

			List<Service<JmsDataSource>> conList = jmsService.getDataSources();
			HashSet<String> existList = new HashSet<>();
			existList.addAll(services.keySet());
			// work over all existing connections
			for (Service<JmsDataSource> src : conList) {
				String conName = src.getService().getName();
				try {
					HeartbeatService service = services.get(conName);
					if (service == null) {
						log().i("create",conName);
						service = new HeartbeatService();
						service.setName(service.getName() + ":" + conName);
						service.setConnectionName(conName);
						jmsService.addChannel(service);
						services.put(conName, service);
						service.doActivate();
						service.doTimerTask(cmd);
					} else {
						if (!service.getChannel().isClosed())
							service.doTimerTask(cmd);
					}
				} catch (Throwable t) {
					log().d(conName,t);
				}
				existList.remove(conName);
			}
			// remove overlapping
			for (String conName : existList) {
				log().i("remove",conName);
				HeartbeatService service = services.get(conName);
				jmsService.removeChannel(service.getName());
				service.doDeactivate();
			}
			
			// send beat ...
			
			JmsManagerService service = JmsUtil.getService();
			if (service == null) return;

			for (JmsDataChannel c : service.getChannels()) {
				try {
					if (c.getChannel() != null) {
						log().d("heart-beat",c,cmd);
						c.getChannel().doBeat();
//						c.getChannel().reset();
//						c.getChannel().open();
					} else
						c.reset();
				} catch (Throwable t) {
					log().w(c,cmd,t);
				}
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

	@Override
	public List<HeartbeatService> getServices() {
		return new LinkedList<>(services.values());
	}
}
