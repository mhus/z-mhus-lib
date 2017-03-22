package de.mhus.lib.karaf.jms;

import java.util.TimerTask;

import de.mhus.lib.jms.JmsConnection;

public class BeatTimerTask extends TimerTask {

	@Override
	public void run() {
		JmsManagerService service = JmsUtil.getService();
		if (service == null) return;
		for (JmsConnection con: service.getConnections()) {
			try {
				con.doChannelBeat();
			} catch (Throwable t) {
				
			}
		}
	}

}
