package de.mhus.lib.karaf.jms;

import java.util.TimerTask;

import de.mhus.lib.jms.JmsConnection;

public class BeatTimerTask extends TimerTask {

	@Override
	public void run() {
		JmsManagerService service = JmsUtil.getService();
		if (service == null) return;
		for (String conName : service.listConnections()) {
			try {
				JmsConnection con = service.getConnection(conName);
				con.doChannelBeat();
			} catch (Throwable t) {
				
			}
		}
	}

}
