package de.mhus.lib.jms;

import java.util.UUID;

import javax.jms.DeliveryMode;
import javax.jms.Session;

import de.mhus.lib.core.config.IConfig;

public abstract class JmsChannel extends JmsObject {

	protected JmsDestination dest;
	protected int deliveryMode = DeliveryMode.NON_PERSISTENT;
	protected int priority = 0; // default
	protected long timeToLive = 60 * 60 * 1000;

	{
		 IConfig cfg = MJms.getConfig();
		timeToLive = cfg.getLong("msgTimeToLive", timeToLive);
	}
	
	public JmsChannel(String destination, boolean destinationTopic) {
		dest = new JmsDestination(destination, destinationTopic);
	}
	
	public JmsChannel(JmsDestination dest) {
		this.dest = dest;
	}
	
	protected String createMessageId() {
		return UUID.randomUUID().toString();
	}

	public JmsDestination getDestination() {
		return dest;
	}

	@Override
	public Session getSession() {
		return dest.getSession();
	}
	
	@Override
	public void close() {
		try {
			dest.getConnection().unregisterChannel(this);
		} catch (Throwable t) {log().t(t);}
		super.close();
	}

	@Override
	public String toString() {
		return getName() + "/" + getClass().getSimpleName();
	}
	
	public abstract void doBeat();
	public abstract String getName();

	public void checkConnection() {
	}

	public void reset(JmsDestination dest) {
		this.dest = dest;
		reset();
	}

	public boolean isDeliveryModePersistent() {
		return deliveryMode == DeliveryMode.PERSISTENT;
	}

	public void setDeliveryModePersistent(boolean persistent) {
		this.deliveryMode = persistent ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

}
