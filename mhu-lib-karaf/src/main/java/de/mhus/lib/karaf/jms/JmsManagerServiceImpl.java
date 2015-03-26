package de.mhus.lib.karaf.jms;

import java.util.Dictionary;
import java.util.HashMap;

import javax.jms.JMSException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import de.mhus.lib.jms.JmsConnection;

@Component(name="JmsManagerService",immediate=true)
public class JmsManagerServiceImpl implements JmsManagerService {

	private HashMap<String, JmsConnection> connections = new HashMap<>();
	private ServiceTracker<JmsDataSource, JmsDataSource> tracker;
	private BundleContext context;
	
	@Activate
	public void doActivate(ComponentContext ctx) {
		context = ctx.getBundleContext();
		tracker = new ServiceTracker<>(context, JmsDataSource.class, new MyServiceTrackerCustomizer() );
		tracker.open();
	}
	
	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		tracker.close();
		for (String name : listConnections())
			removeConnection(name);
	}
	
	@Override
	public void addConnection(String name, JmsConnection con) {
		synchronized (this) {
			connections.put(name, con);
		}
	}

	@Override
	public void addConnection(String name, String url, String user,
			String password) throws JMSException {
		addConnection(name, new JmsConnection(url, user, password));
	}

	@Override
	public String[] listConnections() {
		synchronized (this) {
			return connections.keySet().toArray(new String[0]);
		}
	}

	@Override
	public JmsConnection getConnection(String name) {
		synchronized (this) {
			return connections.get(name);
		}
	}

	@Override
	public void removeConnection(String name) {
		synchronized (this) {
			JmsConnection old = connections.remove(name);
			if (old != null)
				old.close();
		}
	}

	private class MyServiceTrackerCustomizer implements ServiceTrackerCustomizer<JmsDataSource, JmsDataSource> {

		@Override
		public JmsDataSource addingService(
				ServiceReference<JmsDataSource> reference) {
			
			JmsDataSource service = context.getService(reference);
			
			try {
				addConnection(service.getName(), service.createConnection());
			} catch (JMSException e) {
				e.printStackTrace();
			}
			
			return service;
		}

		@Override
		public void modifiedService(ServiceReference<JmsDataSource> reference,
				JmsDataSource service) {
			
			removeConnection(service.getName());
			try {
				addConnection(service.getName(), service.createConnection());
			} catch (JMSException e) {
				e.printStackTrace();
			}
			
		}

		@Override
		public void removedService(ServiceReference<JmsDataSource> reference,
				JmsDataSource service) {
			removeConnection(service.getName());
		}
		
	}
}
