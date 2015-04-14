package de.mhus.lib.karaf.jms;

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
import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsConnection;

@Component(name="JmsManagerService",immediate=true)
public class JmsManagerServiceImpl extends MLog implements JmsManagerService {

	private HashMap<String, JmsConnection> connections = new HashMap<>();
	private ServiceTracker<JmsDataSource, JmsDataSource> connectionTracker;
	
	private HashMap<String, JmsDataChannel> channels = new HashMap<>();
	private ServiceTracker<JmsDataChannel, JmsDataChannel> channelTracker;
	
	private BundleContext context;
	
	@Activate
	public void doActivate(ComponentContext ctx) {
		context = ctx.getBundleContext();
		connectionTracker = new ServiceTracker<>(context, JmsDataSource.class, new MyConnectionTrackerCustomizer() );
		connectionTracker.open();
		
		channelTracker = new ServiceTracker<>(context, JmsDataChannel.class, new MyChannelTrackerCustomizer() );
		channelTracker.open();
	}
	
	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
		connectionTracker.close();
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
	
	public void addChannel(String name, String connectionName, JmsChannel channel) {
		addChannel(new JmsDataChannelImpl(name, connectionName, channel));
	}

	@Override
	public void addChannel(JmsDataChannel channel) {
		synchronized (channels) {
			channels.put(channel.getName(), channel);
		}
		channel.reset(this);
	}

	public void resetChannels() {
		synchronized (channels) {
			for (JmsDataChannel channel : channels.values())
				try {
					channel.reset(this);
				} catch (Throwable t) {
					log().t(channel.getName(),t);
				}
		}
	}
	private class MyConnectionTrackerCustomizer implements ServiceTrackerCustomizer<JmsDataSource, JmsDataSource> {

		@Override
		public JmsDataSource addingService(
				ServiceReference<JmsDataSource> reference) {
			
			JmsDataSource service = context.getService(reference);
			
			try {
				addConnection(service.getName(), service.createConnection());
			} catch (JMSException e) {
				log().t(e);
			}
			resetChannels();
			
			return service;
		}

		@Override
		public void modifiedService(ServiceReference<JmsDataSource> reference,
				JmsDataSource service) {
			
			removeConnection(service.getName());
			try {
				addConnection(service.getName(), service.createConnection());
			} catch (JMSException e) {
				log().t(e);
			}
			resetChannels();
			
		}

		@Override
		public void removedService(ServiceReference<JmsDataSource> reference,
				JmsDataSource service) {
			removeConnection(service.getName());
			resetChannels();
		}
		
	}
	
	private class MyChannelTrackerCustomizer implements ServiceTrackerCustomizer<JmsDataChannel, JmsDataChannel> {

		@Override
		public JmsDataChannel addingService(
				ServiceReference<JmsDataChannel> reference) {

			JmsDataChannel service = context.getService(reference);
			addChannel(service);
			return service;
		}

		@Override
		public void modifiedService(ServiceReference<JmsDataChannel> reference,
				JmsDataChannel service) {

			removeChannel(service.getName());
			addChannel(service);
		}

		@Override
		public void removedService(ServiceReference<JmsDataChannel> reference,
				JmsDataChannel service) {
			removeChannel(service.getName());
		}
		
	}

	@Override
	public String[] listChannels() {
		synchronized (channels) {
			return channels.keySet().toArray(new String[channels.size()]);
		}
	}

	@Override
	public JmsDataChannel getChannel(String name) {
		synchronized (channels) {
			return channels.get(name);
		}
	}

	@Override
	public void removeChannel(String name) {
		synchronized (channels) {
			channels.remove(name);
		}
	}

	@Override
	public <I> I getObjectForInterface(Class<? extends I> ifc) {
		synchronized (channels) {
			
			{
				JmsDataChannel channel = channels.get(ifc.getCanonicalName());
				if (channel != null)
					try {
						I o = channel.getObject(ifc);
						if (o != null) return o;
					} catch (Throwable t) {}
			}
			
			for (JmsDataChannel channel : channels.values()) {
				try {
					I o = channel.getObject(ifc);
					if (o != null) return o;
				} catch (Throwable t) {}
			}
			throw new NotFoundException("object for interface not found", ifc);
		}
	}

	@Override
	public void doChannelBeat() {
		synchronized (this) {
			for (JmsConnection con : connections.values())
				try {
					con.doChannelBeat();
				} catch (Throwable t) {
					log().t(con,t);
				}
		}
	}
	
}
