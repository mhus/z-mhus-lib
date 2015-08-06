package de.mhus.lib.core.schedule;

import java.util.Date;
import java.util.Observer;

public class OnceJob extends SchedulerJob {

	private long time;

	public OnceJob(Date time, Observer task) {
		this(time.getTime(), task);
	}
	
	public OnceJob(long time, Observer task) {
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
