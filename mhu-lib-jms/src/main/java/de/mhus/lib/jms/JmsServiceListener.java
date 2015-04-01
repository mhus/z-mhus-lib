package de.mhus.lib.jms;

public interface JmsServiceListener {

	void jmsServiceOnOpen(JmsChannel channel);
	void jmsServiceOnReset(JmsChannel channel);
	
}
