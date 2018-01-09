package de.mhus.lib.core.schedule;

import java.util.Date;

import de.mhus.lib.core.ITimerTask;
import de.mhus.lib.core.MDate;

public class OnceJob extends SchedulerJob implements MutableSchedulerJob {

	private long time;

	public OnceJob(Date time, ITimerTask task) {
		this(time.getTime(), task);
	}
	
	public OnceJob(long time, ITimerTask task) {
		super(task);
		this.time = time;
	}

	@Override
	public void doCaclulateNextExecution() {
		if (isDone())
			setNextExecutionTime(REMOVE_TIME);
		else
			setNextExecutionTime(time);
	}

	@Override
	public void setDone(boolean done) {
		super.setDone(done);
		if(done)
			cancel();
	}

	@Override
	public String toString() {
		return OnceJob.class.getSimpleName() + "," + isDone() + "," + MDate.toIsoDateTime(time);
	}

	@Override
	public void doReschedule(Scheduler queue, long time) {
		super.doReschedule(queue, time);
	}

	@Override
	public boolean doReconfigure(String config) {
		return false;
	}
	
	@Override
	public void setScheduledTime(long scheduledTime) {
		super.setScheduledTime(scheduledTime);
	}

}
