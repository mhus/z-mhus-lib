package de.mhus.lib.karaf.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import org.osgi.framework.FrameworkUtil;

import aQute.bnd.annotation.component.Reference;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.jms.ClientJsonService;
import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsChannelService;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.JmsDestination;
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
	private boolean initialized = false;
	
	@Reference
	public void setJmsManagerService(JmsManagerService manager) {
		reset(manager);
	}
	
	public JmsDataChannelImpl() {
		name = getClass().getCanonicalName();
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
		initialized=true;
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
		initialized = false;
	}

	public boolean isDestinationTopic() {
		return destinationTopic;
	}

	public void setDestinationTopic(boolean destinationTopic) {
		this.destinationTopic = destinationTopic;
		initialized = false;
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
		if (service == null) {
			log().d("JmsManagerService not found");
			return;
		}
		JmsConnection con = service.getConnection(connectionName);
		
		if (channel == null) {
			if (MString.isSet(implementation)) {
				try {
					Class<?> clazz = FrameworkUtil.getBundle(JmsDataChannel.class).loadClass(implementation);
					JmsDestination dest = new JmsDestination(getDestination(), isDestinationTopic());
					if (JmsChannel.class.isAssignableFrom(clazz)) {
						channel = (JmsChannel)clazz.newInstance();
						initialized = false;
					} else {
						Object o = clazz.newInstance();
						WebServiceDescriptor descriptor = new WebServiceDescriptor(o);
						channel = new ServerJsonService<Object>(dest, descriptor);
						initialized = true;
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
					initialized = true;
				} catch (Throwable e) {
					log().w(e);
				}
			} else {
				channel = new ChannelWrapper(this);
			}
//			} else {
//				log().w("No channel defined for",getClass().getCanonicalName());
//			}
		}
		if (channel != null) {
			if (!initialized && channel.getDestination() == null) {
				JmsDestination dest = new JmsDestination(getDestination(), isDestinationTopic());
				channel.reset(dest);
			}
			initialized = true;
			if (channel.getDestination() != null)
				channel.getDestination().setConnection(con);
			channel.checkConnection();
		}
		doAfterReset();
	}

	protected void doAfterReset() {
	}

	@Override
	public String getInformation() {
		try {
			Class<?> i = getInterface();
			if (i != null)
				return i.getCanonicalName();
		} catch (Throwable t) {}
		try {
			Object o = getObject(Object.class);
			if (o != null)
				return o.toString();
		} catch (Throwable t) {}
		JmsChannel c = getChannel();
		if (c != null)
			return c.toString();
		return "";
	}

	protected void receivedOneWay(Message msg) throws JMSException {
		received(msg);
	}

	protected Message received(Message msg) throws JMSException {
		log().i("received not processed msg",msg);
		return null;
	}

	@Override
	public String toString() {
		return MSystem.toString(this, name,connectionName);
	}
}
