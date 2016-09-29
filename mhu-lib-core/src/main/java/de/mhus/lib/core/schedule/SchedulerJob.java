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

/**
 * <p>Abstract SchedulerJob class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class SchedulerJob extends MTimerTask implements Operation {

	/** Constant <code>CALCULATE_NEXT=0</code> */
	public static final long CALCULATE_NEXT = 0;
	/** Constant <code>DISABLED_TIME=-1</code> */
	public static final long DISABLED_TIME = -1;
	/** Constant <code>REMOVE_TIME=-2</code> */
	public static final long REMOVE_TIME = -2;
	
	/** Constant <code>log</code> */
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
	
	/**
	 * <p>Constructor for SchedulerJob.</p>
	 *
	 * @param task a {@link java.util.Observer} object.
	 */
	public SchedulerJob(Observer task) {
		setTask(task);
		if (task == null)
			setName("null");
		if (task instanceof Named)
			setName(((Named)task).getName());
		else
			setName(MSystem.getClassName(task));
	}
	
	/**
	 * <p>Constructor for SchedulerJob.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param task a {@link java.util.Observer} object.
	 */
	public SchedulerJob(String name,  Observer task) {
		setTask(task);
		setName(name);
	}
	
	/**
	 * Call this method to fire ticks to the scheduler. If the time is come the scheduler will execute the operation and set 'done' to true.
	 *
	 * @param forced a boolean.
	 */
	public void doTick(boolean forced) {
		
		if (!forced) {
			if (!isCanceled() && task instanceof MTimerTask && ((MTimerTask)task).isCanceled())
				cancel();
			if (isCanceled()) return;
			
			if (getNextExecutionTime() == CALCULATE_NEXT) {
				synchronized (this) {
					doCaclulateNextExecution();
				}
			}
		}
		
		if (forced || isExecutionTimeReached()) {
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

	/** {@inheritDoc} */
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
	
	/**
	 * <p>doExecute2.</p>
	 *
	 * @param context a {@link de.mhus.lib.core.strategy.TaskContext} object.
	 * @return a {@link de.mhus.lib.core.strategy.OperationResult} object.
	 * @throws java.lang.Exception if any.
	 */
	protected OperationResult doExecute2(TaskContext context) throws Exception {
		if (task != null) task.update(null, context);
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isBusy() {
		synchronized (this) {
			return owner != null;
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean setBusy(Object owner) {
		synchronized (this) {
			if (this.owner != null) return false;
			this.owner = owner;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean releaseBusy(Object owner) {
		synchronized (this) {
			if (this.owner == null) return true;
			if (this.owner != owner) return false;
			this.owner = null;
		}
		return true;
	}

	/**
	 * <p>Getter for the field <code>owner</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getOwner() {
		return owner;
	}
	
	/**
	 * By default the function will compare the nextExecutionTime with the current time. If the time is come it will return
	 * true. If nextExecutionTime is 0 or less it will return false in every case.
	 *
	 * @return a boolean.
	 */
	protected boolean isExecutionTimeReached() {
		return getNextExecutionTime() > 0 && System.currentTimeMillis() >= getNextExecutionTime();
	}
	
	/**
	 * <p>isDone.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isDone() {
		return done;
	}

	/**
	 * <p>Setter for the field <code>done</code>.</p>
	 *
	 * @param done a boolean.
	 */
	protected void setDone(boolean done) {
		this.done = done;
	}

	protected static class MyTaskContext extends DefaultTaskContext {
		
		public void clear() {
			errorMessage = null;
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasAccess() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean canExecute(TaskContext context) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public OperationDescription getDescription() {
		return null;
	}

	/**
	 * <p>Getter for the field <code>task</code>.</p>
	 *
	 * @return a {@link java.util.Observer} object.
	 */
	public Observer getTask() {
		return task;
	}

	/**
	 * <p>Setter for the field <code>task</code>.</p>
	 *
	 * @param task a {@link java.util.Observer} object.
	 */
	protected void setTask(Observer task) {
		this.task = task;
	}

	/** {@inheritDoc} */
	@Override
	public void doit() throws Exception {
		doTick(false);
	}

	/**
	 * <p>Getter for the field <code>nextExecutionTime</code>.</p>
	 *
	 * @return a long.
	 */
	public long getNextExecutionTime() {
		return nextExecutionTime;
	}

	/**
	 * <p>doError.</p>
	 *
	 * @param t a {@link java.lang.Throwable} object.
	 */
	protected void doError(Throwable t) {
		log.e(getName(),t);
	}

	/**
	 * <p>doSchedule.</p>
	 *
	 * @param scheduler a {@link de.mhus.lib.core.schedule.Scheduler} object.
	 */
	protected void doSchedule(Scheduler scheduler) {
		doReschedule(scheduler, getNextExecutionTime());
	}

	/**
	 * <p>doReschedule.</p>
	 *
	 * @param scheduler a {@link de.mhus.lib.core.schedule.Scheduler} object.
	 * @param time a long.
	 */
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

	/**
	 * <p>Getter for the field <code>lastExecutionStart</code>.</p>
	 *
	 * @return a long.
	 */
	public long getLastExecutionStart() {
		return lastExecutionStart;
	}

	/**
	 * <p>Getter for the field <code>lastExecutionStop</code>.</p>
	 *
	 * @return a long.
	 */
	public long getLastExecutionStop() {
		return lastExecutionStop;
	}

	/**
	 * <p>Getter for the field <code>scheduledTime</code>.</p>
	 *
	 * @return a long.
	 */
	public long getScheduledTime() {
		return scheduledTime;
	}

	/**
	 * <p>Setter for the field <code>scheduledTime</code>.</p>
	 *
	 * @param scheduledTime a long.
	 */
	protected void setScheduledTime(long scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	/** {@inheritDoc} */
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

	/**
	 * <p>Getter for the field <code>timeoutInMinutes</code>.</p>
	 *
	 * @return a long.
	 */
	public long getTimeoutInMinutes() {
		return timeoutInMinutes;
	}

	/**
	 * <p>Setter for the field <code>timeoutInMinutes</code>.</p>
	 *
	 * @param timeoutInMinutes a long.
	 */
	public void setTimeoutInMinutes(long timeoutInMinutes) {
		this.timeoutInMinutes = timeoutInMinutes;
	}

	/**
	 * If you have defined a timeout, the event will be called every minute. Be carefully with the execution time of this
	 * event, it can hem the hole scheduler.
	 */
	public void doTimeoutReached() {
		
	}

	/**
	 * <p>Getter for the field <code>thread</code>.</p>
	 *
	 * @return a {@link java.lang.Thread} object.
	 */
	public Thread getThread() {
		return thread;
	}

	/**
	 * <p>Setter for the field <code>nextExecutionTime</code>.</p>
	 *
	 * @param nextExecutionTime a long.
	 */
	public void setNextExecutionTime(long nextExecutionTime) {
		this.nextExecutionTime = nextExecutionTime;
	}

}
