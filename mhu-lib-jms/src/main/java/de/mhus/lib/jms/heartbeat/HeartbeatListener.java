package de.mhus.lib.jms.heartbeat;

import de.mhus.lib.annotations.activator.DefaultImplementationNull;

@DefaultImplementationNull
public interface HeartbeatListener {

	void heartbeatReceived(String txt);

}
