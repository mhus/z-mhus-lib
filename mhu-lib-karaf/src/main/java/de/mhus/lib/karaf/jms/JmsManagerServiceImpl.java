package de.mhus.lib.karaf.jms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;

import javax.jms.JMSException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.base.service.TimerFactory;
import de.mhus.lib.core.base.service.TimerIfc;
import de.mhus.lib.core.cfg.CfgInt;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.karaf.MOsgi;
import de.mhus.lib.karaf.MOsgi.Service;

/**
 * Note: because of a 'new behavior' or bug in Felix we need to delay the start of the
 * service trackers. Immediately will cause an 'Circular reference detected' exception.
 * 
 * Discussion: https://github.com/eclipse/smarthome/issues/870
 * 
 * Switching to DS driven references was also not successful, got the same exception.
 * 
 * You can chnage the behavior in mhus-config by setting JmsManagerService/startupDelay to zero
 * 
 * @author mikehummel
 *
 */
@Component(name="JmsManagerService",immediate=true)
public class JmsManagerServiceImpl extends MLog implements JmsManagerService {

//	private static CfgInt startupDelay = new CfgInt(JmsManagerService.class, "startupDelay", 10000);
	
//	private HashMap<String, JmsConnection> connections = new HashMap<>();
//	private ServiceTracker<JmsDataSource, JmsDataSource> connectionTracker;
	
//	private HashMap<String, JmsDataChannel> channels = new HashMap<>();
//	private ServiceTracker<JmsDataChannel, JmsDataChannel> channelTracker;
	
//	private BundleContext context;
//	private TimerIfc timer;
	
	@Activate
	public void doActivate(ComponentContext ctx) {
//		context = ctx.getBundleContext();
//		if (startupDelay.value() <= 0)
//			initializeTracker();
	}
	
	@Deactivate
	public void doDeactivate(ComponentContext ctx) {
//		if (channelTracker != null) channelTracker.close();
//		if (connectionTracker != null) connectionTracker.close();
//		for (String name : listConnections())
//			removeConnection(name);
//		if (timer != null) timer.cancel();
	}
	
//	@Reference(service=TimerFactory.class)
//	public void setTimerFactory(TimerFactory factory) {
//		
//		if (startupDelay.value() <= 0 || connectionTracker != null) return;
//		
//		timer = factory.getTimer();
//		timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				initializeTracker();
//			}
//			
//		}, startupDelay.value());
//	}
	
//	protected void initializeTracker() {
//		log().i("initialize tracker");
//		connectionTracker = new ServiceTracker<>(context, JmsDataSource.class, new MyConnectionTrackerCustomizer() );
//		connectionTracker.open();
//		
//		channelTracker = new ServiceTracker<>(context, JmsDataChannel.class, new MyChannelTrackerCustomizer() );
//		channelTracker.open();
//	}

//	@Reference(service=JmsDataSource.class,dynamic=true,multiple=true,unbind="removeJmsDataSource")
//	public void addJmsDataSource(JmsDataSource dataSource) {
//		
//		try {
//			addConnection(dataSource.getName(), dataSource.createConnection());
//		} catch (JMSException e) {
//			log().e(dataSource.getName(), e);
//		}
//
//	}
//
//	public void removeJmsDataSource(JmsDataSource dataSource) {
//		removeConnection(dataSource.getName());
//	}
//	
//	@Reference(service=JmsDataChannel.class,dynamic=true,multiple=true,unbind="removeJmsDataChannel")
//	public void addJmsDataChannel(JmsDataChannel dataChannel) {
//		addChannel(dataChannel);
//	}
//
//	public void removeJmsDataChannel(JmsDataChannel dataChannel) {
//		removeChannel(dataChannel.getName());
//	}
	
	
//	@Override
//	public void addConnection(String name, JmsConnection con) {
//		log().d("add connection",name);
//		synchronized (this) {
//			connections.put(name, con);
//		}
//	}
//
//	@Override
//	public void addConnection(String name, String url, String user,
//			String password) throws JMSException {
//		addConnection(name, new JmsConnection(url, user, password));
//	}

	@Override
	public String[] listConnections() {
//		synchronized (this) {
//			return connections.keySet().toArray(new String[0]);
//		}
		LinkedList<String> out = new LinkedList<>();
		for (MOsgi.Service<JmsDataSource> ref : MOsgi.getServiceRefs(JmsDataSource.class, null)) {
			String name = getServiceName(ref);
			if (name != null)
				out.add(name);
		}
		return out.toArray(new String[out.size()]);
	}

	@Override
	public List<JmsConnection> getConnections() {
//		synchronized (this) {
//			return connections.keySet().toArray(new String[0]);
//		}
		LinkedList<JmsConnection> out = new LinkedList<>();
		for (JmsDataSource obj : MOsgi.getServices(JmsDataSource.class, null))
			try {
				out.add(obj.getConnection());
			} catch (JMSException e) {
				log().w(e);
			}
		return out;
	}

	@Override
	public List<MOsgi.Service<JmsDataSource>> getDataSources() {
//		synchronized (this) {
//			return connections.keySet().toArray(new String[0]);
//		}
		LinkedList<MOsgi.Service<JmsDataSource>> out = new LinkedList<>();
		for (MOsgi.Service<JmsDataSource> obj : MOsgi.getServiceRefs(JmsDataSource.class, null))
			out.add(obj);
		return out;
	}
	
	@Override
	public JmsConnection getConnection(String name) {
//		synchronized (this) {
//			return connections.get(name);
//		}
		try {
			JmsDataSource src = MOsgi.getService(JmsDataSource.class,"(osgi.jndi.service.name=jms_" + name + ")");
			if (src == null) return null;
			return src.getConnection();
		} catch (NotFoundException nfe) { 
			return null;
		} catch (JMSException e) {
			log().w(name,e);
			return null;
		}
	}

//	@Override
//	public void removeConnection(String name) {
//		log().d("remove connection",name);
//		synchronized (this) {
//			JmsConnection old = connections.remove(name);
//			if (old != null)
//				old.close();
//		}
//	}
	
//	public void addChannel(String name, String connectionName, JmsChannel channel) {
//		addChannel(new JmsDataChannelImpl(name, connectionName, channel));
//	}

//	@Override
//	public void addChannel(JmsDataChannel channel) {
//		log().d("add channel",channel.getName());
//		synchronized (channels) {
//			channels.put(channel.getName(), channel);
//		}
//		channel.reset(this);
//	}

	@Override
	public void resetChannels() {
//		synchronized (channels) {
		List<JmsDataChannel> channels = getChannels();
		for (JmsDataChannel channel : channels)
			try {
				channel.reset(this);
			} catch (Throwable t) {
				log().t(channel.getName(),t);
			}
//		}
	}
/*
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

			try {
				JmsDataChannel service = context.getService(reference);
				if (service != null)
					addChannel(service);
				return service;
			} catch (Throwable t) {
				log().e(reference,t);
				return null;
			}
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
*/
	@Override
	public String[] listChannels() {
//		synchronized (channels) {
//			return channels.keySet().toArray(new String[channels.size()]);
//		}
		LinkedList<String> out = new LinkedList<>();
		for (MOsgi.Service<JmsDataChannel> ref : MOsgi.getServiceRefs(JmsDataChannel.class, null))
			out.add(ref.getName());
		return out.toArray(new String[out.size()]);
	}

	@Override
	public JmsDataChannel getChannel(String name) {
//		synchronized (channels) {
//			return channels.get(name);
//		}
		return MOsgi.getService(JmsDataChannel.class,MOsgi.filterServiceName(name));
	}

	@Override
	public List<JmsDataChannel> getChannels() {
//		synchronized (channels) {
//			return channels.get(name);
//		}
		LinkedList<JmsDataChannel> out = new LinkedList<>();
		for (JmsDataChannel obj : MOsgi.getServices(JmsDataChannel.class, null))
			out.add(obj);
		return out;
	}
	
//	@Override
//	public void removeChannel(String name) {
//		log().d("remove channel",name);
//		synchronized (channels) {
//			channels.remove(name);
//		}
//	}

	@Override
	public <I> I getObjectForInterface(Class<? extends I> ifc) {
//		synchronized (channels) {
			
			{
				JmsDataChannel channel = getChannel(ifc.getCanonicalName());
				if (channel != null)
					try {
						I o = channel.getObject(ifc);
						if (o != null) return o;
					} catch (Throwable t) {}
			}
			
			for (JmsDataChannel channel : getChannels()) {
				try {
					I o = channel.getObject(ifc);
					if (o != null) return o;
				} catch (Throwable t) {}
			}
			throw new NotFoundException("object for interface not found", ifc);
//		}
	}

	@Override
	public void doChannelBeat() {
//		synchronized (this) {
			for (JmsConnection con : getConnections())
				try {
					con.doChannelBeat();
				} catch (Throwable t) {
					log().t(con,t);
				}
//		}
	}

	@Override
	public String getServiceName(Service<JmsDataSource> ref) {
		Object p = ref.getReference().getProperty("osgi.jndi.service.name");
		if (p != null && p instanceof String && ((String)p).length() > 4 && ((String)p).startsWith("jms_"))
			return ((String)p).substring(4);
		return null;
	}
	
}
