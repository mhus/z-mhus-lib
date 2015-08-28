package de.mhus.lib.core.schedule;

import java.util.Observer;

import de.mhus.lib.basics.Named;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.strategy.DefaultTaskContext;
import de.mhus.lib.core.strategy.NotSuccessful;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.OperationDescription;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.TaskContext;

public abstract class SchedulerJob extends MTimerTask implements Operation {

	public static final long CALCULATE_NEXT = 0;
	public static final long DISABLED_TIME = -1;
	public static final long REMOVE_TIME = -2;
	
	protected static Log log = Log.getLog(SchedulerJob.class);
	private Object owner;
	private long nextExecutionTime = CALCULATE_NEXT;
	protected MyTaskContext context = new MyTaskContext();
	private boolean done = false;
	private Observer task;
	private long lastExecutionStart;
	private long lastExecutionStop;
	private long scheduledTime;
	private long timeoutInMinutes;
	private Thread thread;
	
	public SchedulerJob(Observer task) {
		setTask(task);
		if (task == null)
			setName("null");
		if (task instanceof Named)
			setName(((Named)task).getName());
		else
			setName(MSystem.getObjectId(task));
	}
	
	public SchedulerJob(String name,  Observer task) {
		setTask(task);
		setName(name);
	}
	
	/**
	 * Call this method to fire ticks to the scheduler. If the time is come the scheduler will execute the operation and set 'done' to true.
	 */
	public void doTick() {
		
		if (!isCanceled() && task instanceof MTimerTask && ((MTimerTask)task).isCanceled())
			cancel();
		if (isCanceled()) return;
		
		if (getNextExecutionTime() == CALCULATE_NEXT) {
			synchronized (this) {
				doCaclulateNextExecution();
			}
		}
		
		if (isExecutionTimeReached()) {
			lastExecutionStart = System.currentTimeMillis();
			thread = Thread.currentThread();
			try {
				doExecute(context);
			} catch (Throwable e) {
				doError(e);
			}
			thread = null;
			lastExecutionStop = System.currentTimeMillis();
			context.clear();
			setDone(true);
			synchronized (this) {
				doCaclulateNextExecution();
			}
		}
	}

	/**
	 * Calculate the next executionTime and store it into nextExecutionTime
	 */
	protected abstract void doCaclulateNextExecution();

	@Override
	public final OperationResult doExecute(TaskContext context) throws Exception {
		log.d("execute",context.getParameters());
		if (!hasAccess()) {
			log.d("access denied",context,context.getErrorMessage());
			return new NotSuccessful(this, "access denied", OperationResult.ACCESS_DENIED);
		}
		if (!canExecute(context)) {
			log.d("execution denied",context.getErrorMessage());
			return new NotSuccessful(this, context.getErrorMessage() != null ? context.getErrorMessage() : "can't execute", OperationResult.NOT_EXECUTABLE);
		}
		OperationResult ret = doExecute2(context);
		log.d("result",ret);
		return ret;
	}
	
	protected OperationResult doExecute2(TaskContext context) throws Exception {
		if (task != null) task.update(null, context);
		return null;
	}

	@Override
	public boolean isBusy() {
		synchronized (this) {
			return owner != null;
		}
	}

	@Override
	public boolean setBusy(Object owner) {
		synchronized (this) {
			if (this.owner != null) return false;
			this.owner = owner;
		}
		return true;
	}

	@Override
	public boolean releaseBusy(Object owner) {
		synchronized (this) {
			if (this.owner == null) return true;
			if (this.owner != owner) return false;
			this.owner = null;
		}
		return true;
	}

	public Object getOwner() {
		return owner;
	}
	
	/**
	 * By default the function will compare the nextExecutionTime with the current time. If the time is come it will return
	 * true. If nextExecutionTime is 0 or less it will return false in every case.
	 * @return
	 */
	protected boolean isExecutionTimeReached() {
		return getNextExecutionTime() > 0 && System.currentTimeMillis() >= getNextExecutionTime();
	}
	
	public boolean isDone() {
		return done;
	}

	protected void setDone(boolean done) {
		this.done = done;
	}

	protected static class MyTaskContext extends DefaultTaskContext {
		
		public void clear() {
			errorMessage = null;
		}
	}
	
	@Override
	public boolean hasAccess() {
		return true;
	}

	@Override
	public boolean canExecute(TaskContext context) {
		return true;
	}

	@Override
	public OperationDescription getDescription() {
		return null;
	}

	public Observer getTask() {
		return task;
	}

	protected void setTask(Observer task) {
		this.task = task;
	}

	@Override
	public void doit() throws Exception {
		doTick();
	}

	public long getNextExecutionTime() {
		return nextExecutionTime;
	}

	protected void doError(Throwable t) {
		log.e(getName(),t);
	}

	protected void doSchedule(Scheduler scheduler) {
		doReschedule(scheduler, getNextExecutionTime());
	}

	protected void doReschedule(Scheduler scheduler, long time) {
		setNextExecutionTime(time);
		if (isCanceled()) return;
		if (getNextExecutionTime() == CALCULATE_NEXT)
			doCaclulateNextExecution();
		if (getNextExecutionTime() == REMOVE_TIME) {
			return;
		}
		if (getNextExecutionTime() == DISABLED_TIME) {
			setScheduledTime(System.currentTimeMillis() + MTimeInterval.DAY_IN_MILLISECOUNDS); // schedule tomorrow
			scheduler.getQueue().removeJob(this);
			scheduler.getQueue().doSchedule(this);
			return;
		}
		setScheduledTime(getNextExecutionTime());
		scheduler.getQueue().removeJob(this);
		scheduler.getQueue().doSchedule(this);
	}

	public long getLastExecutionStart() {
		return lastExecutionStart;
	}

	public long getLastExecutionStop() {
		return lastExecutionStop;
	}

	public long getScheduledTime() {
		return scheduledTime;
	}

	protected void setScheduledTime(long scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	@Override
	public String toString() {
//		return task.getClass().getName() + "," + 
//				getClass().getName() + "," + 
//				MDate.toIsoDateTime(scheduledTime) + "," + 
//				MDate.toIsoDateTime(nextExecutionTime) + "," + 
//				isCanceled() + "," + 
//				owner + ","+
//				MDate.toIsoDateTime(getLastExecutionStart()) + "," + 
//				MDate.toIsoDateTime(getLastExecutionStop());
		return 
			getClass().getName();
	}

	public long getTimeoutInMinutes() {
		return timeoutInMinutes;
	}

	public void setTimeoutInMinutes(long timeoutInMinutes) {
		this.timeoutInMinutes = timeoutInMinutes;
	}

	/**
	 * If you have defined a timeout, the event will be called every minute. Be carefully with the execution time of this
	 * event, it can hem the hole scheduler.
	 */
	public void doTimeoutReached() {
		
	}

	public Thread getThread() {
		return thread;
	}

	public void setNextExecutionTime(long nextExecutionTime) {
		this.nextExecutionTime = nextExecutionTime;
	}

}
