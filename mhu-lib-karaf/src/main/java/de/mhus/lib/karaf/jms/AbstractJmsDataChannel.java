package de.mhus.lib.karaf.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.ComponentContext;

import aQute.bnd.annotation.component.Reference;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.errors.NotFoundRuntimeException;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.jms.ClientJsonProxy;
import de.mhus.lib.jms.JmsChannel;
import de.mhus.lib.jms.JmsObjectProxy;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.JmsDestination;
import de.mhus.lib.jms.ServerJsonProxy;
import de.mhus.lib.jms.WebServiceDescriptor;

public abstract class AbstractJmsDataChannel extends MLog implements JmsDataChannel {

	private JmsChannel channel;
	protected String name;
	protected String connectionName;
	
	public AbstractJmsDataChannel() {
		name = getClass().getSimpleName();
	}
	
	@Override
	public JmsChannel getChannel() {
		try {
			checkChannel();
		} catch (JMSException e) {
			log().d(getName(),getConnectionName(),e);
		}
		return channel;
	}
	
	public synchronized void checkChannel() throws JMSException {
		if (channel == null)
			channel = createChannel();
	}
	
	/**
	 * Create a new channel object of the handling channel.
	 * 
	 * @return
	 * @throws JMSException
	 */
	protected abstract JmsChannel createChannel() throws JMSException;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getConnectionName() {
		return connectionName;
	}

	@Override
	public void reset() {
		log().d("reset",getName(),getConnectionName());
		onDisconnect();
		channel = null;
		onConnect();
	}

	@Override
	public void onConnect() {
		JmsConnection con = JmsUtil.getConnection(getConnectionName());
		if (con != null) {
			if (!con.isConnected())
				try {
					con.open();
				} catch (JMSException e1) {
					log().w(getName(),getConnectionName(),e1);
					return;
				}
			if (!con.isConnected()) {
				log().d("not connected",getConnectionName());
				return;
			}
			if (channel == null) {
				try {
					checkChannel();
					channel.getJmsDestination().setConnection(con);
					channel.open();
				} catch (JMSException e) {
					log().w(getName(),getConnectionName(),e);
				}
			} else {
				channel.getJmsDestination().setConnection(con);
				try {
					channel.reopen();
				} catch (JMSException e) {
					log().w(getName(),getConnectionName(),e);
				}
			}
		} else {
			log().d("connection not found", getName(), getConnectionName());
		}
	}

	@Override
	public void onDisconnect() {
		if (channel == null) return;
		channel.close();
	}
	
	@Override
	public void doBeat() {
		if (channel == null || channel.isClosed()) {
			onConnect();
			channel.isConnected();
		} else 
		if (!channel.isClosed() && !channel.isConnected()) {
			MApi.lookup(JmsManagerService.class).resetConnection(getConnectionName());
			onConnect();
		}
	}

}
