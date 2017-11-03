package de.mhus.lib.karaf.jms.heartbeat;

import java.util.List;

public interface HeartbeatAdmin {
	
	void sendHeartbeat(String cmd);
	
	void setEnabled(boolean enable);
	
	boolean isEnabled();

	List<HeartbeatService> getServices();
	
}
