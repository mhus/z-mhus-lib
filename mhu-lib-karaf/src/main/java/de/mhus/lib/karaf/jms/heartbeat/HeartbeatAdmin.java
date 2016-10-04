package de.mhus.lib.karaf.jms.heartbeat;

public interface HeartbeatAdmin {

	void sendHeartbeat();
	
	void sendHeartbeat(String cmd);
	
	void setEnabled(boolean enable);
	
	boolean isEnabled();
	
}
