package de.mhus.lib.core.strategy;

import de.mhus.lib.core.logging.Log;

public class DefaultMonitor implements Monitor {

	static Log log = Log.getLog(DefaultMonitor.class);
	
	@Override
	public void println() {
	}

	@Override
	public void println(Object... out) {
		log.i(out);
	}

	@Override
	public void print(Object... out) {
		log.i(out);
	}

	@Override
	public Log log() {
		return log;
	}

	@Override
	public void setEstimatedSteps(long steps) {
		
	}

	@Override
	public void setCurrentStep(long step) {
		
	}

	@Override
	public void incrementStep() {
		
	}

}
