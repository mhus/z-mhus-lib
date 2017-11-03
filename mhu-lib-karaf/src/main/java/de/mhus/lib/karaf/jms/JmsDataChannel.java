package de.mhus.lib.karaf.jms;

import de.mhus.lib.jms.JmsChannel;

public interface JmsDataChannel {

	JmsChannel getChannel();
	String getName();
	String getConnectionName();
	
	/**
	 * Call to reconnect the channel.
	 */
	void reset();
	
	/**
	 * Called if connection is available
	 */
	void onConnect();
	
	/**
	 * Called if connection disappears
	 */
	void onDisconnect();
	void doBeat();
	
}
