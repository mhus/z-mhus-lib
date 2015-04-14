package de.mhus.lib.karaf.jms;

import org.osgi.framework.FrameworkUtil;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.jms.ClientJsonService;
import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsChannelService;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.JmsDestination;
import de.mhus.lib.jms.ServerJms;
import de.mhus.lib.jms.ServerJsonService;
import de.mhus.lib.jms.WebServiceDescriptor;

public class JmsDataChannelImpl extends MLog implements JmsDataChannel {

	private String name;
	private String connectionName;
	private JmsChannel channel;
	private String implementation;
	private String ifc;
	private String destination;
	private boolean destinationTopic;
	
	public JmsDataChannelImpl() {
	}
	
	public JmsDataChannelImpl(String name, String connectionName, JmsChannel channel) {
		this.name = name;
		this.connectionName = connectionName;
		this.channel = channel;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	@Override
	public JmsChannel getChannel() {
		return channel;
	}

	public void setChannel(JmsChannel channel) {
		this.channel = channel;
	}

	@Override
	public synchronized void reset() {
		reset(JmsUtil.getService());
	}

	public String getImplementation() {
		return implementation;
	}

	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public boolean isDestinationTopic() {
		return destinationTopic;
	}

	public void setDestinationTopic(boolean destinationTopic) {
		this.destinationTopic = destinationTopic;
	}

	public String getIfc() {
		return ifc;
	}

	public void setIfc(String ifc) {
		this.ifc = ifc;
	}

	@Override
	public <I> I getObject(Class<? extends I> ifc) {
		if (channel == null) reset();
		if (channel == null) throw new NotFoundException("channel is null",name);
		if (channel instanceof JmsChannelService)
			return ((JmsChannelService)channel).getObject();
		throw new NotSupportedException("channel is not a service",name);
	}

	@Override
	public Class<?> getInterface() {
		if (channel == null) reset();
		if (channel == null) throw new NotFoundException("channel is null",name);
		if (channel instanceof JmsChannelService)
			return ((JmsChannelService)channel).getInterface();
		throw new NotSupportedException("channel is not a service",name);
	}

	@Override
	public void reset(JmsManagerService service) {
		JmsConnection con = service.getConnection(connectionName);
		
		if (channel == null) {
			if (MString.isSet(implementation)) {
				try {
					Class<?> clazz = FrameworkUtil.getBundle(JmsDataChannel.class).loadClass(implementation);
					JmsDestination dest = new JmsDestination(getDestination(), isDestinationTopic());
					if (JmsChannel.class.isAssignableFrom(clazz)) {
						channel = (JmsChannel)clazz.getConstructor(JmsDestination.class).newInstance(dest);
					} else {
						Object o = clazz.newInstance();
						WebServiceDescriptor descriptor = new WebServiceDescriptor(o);
						channel = new ServerJsonService(dest, descriptor);
					}
				} catch (Throwable e) {
					log().w(e);
				}
			} else
			if (MString.isSet(ifc)) {
				try {
					Object o = Class.forName(ifc);
					WebServiceDescriptor descriptor = new WebServiceDescriptor(o);
					JmsDestination dest = new JmsDestination(getDestination(), isDestinationTopic());
					channel = new ClientJsonService<Object>(dest, descriptor);
				} catch (Throwable e) {
					log().w(e);
				}
			}
		}
		if (channel != null) {
			channel.getDestination().setConnection(con);
			channel.checkConnection();
		}
	}
	
}
