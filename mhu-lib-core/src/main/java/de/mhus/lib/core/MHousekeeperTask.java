package de.mhus.lib.core;

import de.mhus.lib.core.logging.Log;

public abstract class MHousekeeperTask extends MTimerTask {
	
	private Log log = Log.getLog(this.getClass());

	protected Log log() {
		return log;
	}
}
