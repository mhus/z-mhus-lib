package de.mhus.lib.core.schedule;

import java.util.Observable;
import java.util.Observer;

import de.mhus.lib.core.strategy.OperationDescription;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.TaskContext;

/**
 * <p>SchedulerJobProxy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class SchedulerJobProxy extends SchedulerJob implements MutableSchedulerJob {

	private SchedulerJob instance;

	/**
	 * <p>Constructor for SchedulerJobProxy.</p>
	 *
	 * @param instance a {@link de.mhus.lib.core.schedule.SchedulerJob} object.
	 */
	public SchedulerJobProxy(SchedulerJob instance) {
		super(instance.getName(), instance.getTask());
		this.instance = instance;
	}

	/** {@inheritDoc} */
	@Override
	public void doTick(boolean forced) {
		instance.doTick(forced);
	}

	/** {@inheritDoc} */
	@Override
	public void update(Observable o, Object arg) {
		instance.update(o, arg);
	}

	/** {@inheritDoc} */
	@Override
	public boolean cancel() {
		super.cancel();
		return instance.cancel();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCanceled() {
		return super.isCanceled() || instance.isCanceled();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isBusy() {
		return instance.isBusy();
	}

	/** {@inheritDoc} */
	@Override
	public boolean setBusy(Object owner) {
		return instance.setBusy(owner);
	}

	/** {@inheritDoc} */
	@Override
	public boolean releaseBusy(Object owner) {
		return instance.releaseBusy(owner);
	}

	/** {@inheritDoc} */
	@Override
	public long scheduledExecutionTime() {
		return instance.scheduledExecutionTime();
	}

	/** {@inheritDoc} */
	@Override
	public Object getOwner() {
		return instance.getOwner();
	}

	// Enabling this will cause the Scheduler not be able to remove the job out of the running list
//	@Override
//	public boolean equals(Object obj) {
//		return instance.equals(obj);
//	}

	/** {@inheritDoc} */
	@Override
	public boolean isDone() {
		return instance.isDone();
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasAccess() {
		return instance.hasAccess();
	}

	/** {@inheritDoc} */
	@Override
	public boolean canExecute(TaskContext context) {
		return instance.canExecute(context);
	}

	/** {@inheritDoc} */
	@Override
	public OperationDescription getDescription() {
		return instance.getDescription();
	}

	/** {@inheritDoc} */
	@Override
	public Observer getTask() {
		return instance.getTask();
	}

	/** {@inheritDoc} */
	@Override
	public void doit() throws Exception {
		instance.doit();
	}

	/** {@inheritDoc} */
	@Override
	public long getNextExecutionTime() {
		return instance.getNextExecutionTime();
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return instance.getName();
	}

	/** {@inheritDoc} */
	@Override
	public long getLastExecutionStart() {
		return instance.getLastExecutionStart();
	}

	/** {@inheritDoc} */
	@Override
	public long getLastExecutionStop() {
		return instance.getLastExecutionStop();
	}

	/** {@inheritDoc} */
	@Override
	public long getScheduledTime() {
		return instance.getScheduledTime();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return instance.toString();
	}

	/** {@inheritDoc} */
	@Override
	public long getTimeoutInMinutes() {
		return instance.getTimeoutInMinutes();
	}

	/** {@inheritDoc} */
	@Override
	public void setTimeoutInMinutes(long timeoutInMinutes) {
		instance.setTimeoutInMinutes(timeoutInMinutes);
	}

	/** {@inheritDoc} */
	@Override
	public void doTimeoutReached() {
		instance.doTimeoutReached();
	}

	/** {@inheritDoc} */
	@Override
	protected void doCaclulateNextExecution() {
		instance.doCaclulateNextExecution();
	}

	/** {@inheritDoc} */
	@Override
	public void setCanceled(boolean canceled) {
		instance.setCanceled(canceled);
	}
	
//	@Override
//	protected void doSchedule(Scheduler scheduler) {
//		super.doSchedule(scheduler);
//	}

	/** {@inheritDoc} */
	@Override
	public void doReschedule(Scheduler scheduler, long time) {
		super.doReschedule(scheduler, time);
//		instance.doReschedule(scheduler, time);
	}
	
	/** {@inheritDoc} */
	@Override
	protected void doError(Throwable t) {
		instance.doError(t);
	}
	
	/** {@inheritDoc} */
	@Override
	public void setDone(boolean done) {
		instance.setDone(done);
	}
	
	/** {@inheritDoc} */
	@Override
	protected boolean isExecutionTimeReached() {
		return instance.isExecutionTimeReached();
	}
	
	/** {@inheritDoc} */
	@Override
	protected OperationResult doExecute2(TaskContext context) throws Exception {
		return instance.doExecute2(context);
	}

	/** {@inheritDoc} */
	@Override
	public void setNextExecutionTime(long nextExecutionTime) {
		instance.setNextExecutionTime(nextExecutionTime);
	}

	/** {@inheritDoc} */
	@Override
	public void setScheduledTime(long scheduledTime) {
		instance.setScheduledTime(scheduledTime);
	}

	/** {@inheritDoc} */
	@Override
	public boolean doReconfigure(String config) {
		if (instance instanceof MutableSchedulerJob)
			return ((MutableSchedulerJob)instance).doReconfigure(config);
		return false;
	}

	
}
