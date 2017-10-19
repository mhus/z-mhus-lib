package de.mhus.lib.karaf.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Reference;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.JmsDestination;

public abstract class AbstractJmsDataChannel extends MLog implements JmsDataChannel {

	private String name;
	private String connectionName;
	private JmsChannel channel;
	private String destination;
	private boolean destinationTopic;
	private boolean initialized = false;

	public void doActivate(ComponentContext ctx) {
		
		setChannel(createChannel());
		setConnectionName(getJmsConnectionName());
		reset();
//		server.setInterceptorIn(new TicketAccessInterceptor());
	}	
	
	
	public void doDeactivate(ComponentContext ctx) {
		if (getChannel() != null) getChannel().close();
		setChannel(null);
	}
	
	protected abstract JmsChannel createChannel();
	protected abstract String getJmsConnectionName();
	
	@Reference
	public void setJmsManagerService(JmsManagerService manager) {
		reset(manager);
	}
	
	public AbstractJmsDataChannel() {
		name = getClass().getCanonicalName();
	}
	
	public AbstractJmsDataChannel(String connectionName, JmsChannel channel) {
		this(null, connectionName, channel);
	}
	
	public AbstractJmsDataChannel(String name, String connectionName, JmsChannel channel) {
		if (name == null)
			name = getClass().getCanonicalName();
		else
			this.name = name;
		this.connectionName = connectionName;
		this.channel = channel;
		reset();
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

	public String getDestination() {
		return destination;
	}

	@Override
	final public <I> I getObject(Class<? extends I> ifc) {
		throw new NotSupportedException("channel is not a service",name);
	}

	@Override
	final public Class<?> getInterface() {
		throw new NotSupportedException("channel is not a service",name);
	}

	@Override
	public void reset(JmsManagerService service) {
		if (service == null) {
			log().d("JmsManagerService not found");
			return;
		}
		
		service.addChannel(this); // remember me :)
		
		JmsConnection con = service.getConnection(connectionName);
		if (con == null) {
			log().d("connection not found",name,connectionName);
		}
		
		if (channel != null) {
			if (channel.getJmsDestination() != null) {
				channel.getJmsDestination().setConnection(con);
				if (con != null) {
					initialized = true;
					channel.checkConnection();
				}
			}
		}
		doAfterReset();
	}

	protected void doAfterReset() {
	}

	@Override
	public String getInformation() {
		JmsChannel c = getChannel();
		if (c != null)
			return c.toString();
		return "";
	}
	
	@Override
	public String toString() {
		return MSystem.toString(this, name,connectionName);
	}
	
}
