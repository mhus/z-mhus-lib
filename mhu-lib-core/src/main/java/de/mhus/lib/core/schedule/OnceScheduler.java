package de.mhus.lib.core.schedule;

import java.util.Observer;

public class OnceScheduler extends Scheduler {

	private long time;

	public OnceScheduler(long time, Observer task) {
		super(task);
		this.time = time;
	}

	@Override
	public void doCaclulateNextExecution() {
		if (isDone()) 
			nextExecutionTime = DISABLED_TIME;
		else
			nextExecutionTime = time;
	}

	@Override
	public void setDone(boolean done) {
		super.setDone(done);
		if(done)
			cancel();
	}

}
