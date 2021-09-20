/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.schedule;

import java.util.UUID;

import de.mhus.lib.basics.Named;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.ITimerTask;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MTimerTask;
import de.mhus.lib.core.aaa.Aaa;
import de.mhus.lib.core.aaa.SubjectEnvironment;
import de.mhus.lib.core.logging.ITracer;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.operation.DefaultTaskContext;
import de.mhus.lib.core.operation.NotSuccessful;
import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.OperationDescription;
import de.mhus.lib.core.operation.OperationResult;
import de.mhus.lib.core.operation.TaskContext;
import de.mhus.lib.core.util.MNls;
import io.opentracing.References;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;

public abstract class SchedulerJob extends MTimerTask implements Operation {

    public static final long CALCULATE_NEXT = 0;
    public static final long DISABLED_TIME = -1;
    public static final long REMOVE_TIME = -2;

    protected static Log log = Log.getLog(SchedulerJob.class);
    private Object owner;
    private long nextExecutionTime = CALCULATE_NEXT;
    protected MyTaskContext context = new MyTaskContext();
    private boolean done = false;
    private ITimerTask task;
    private long lastExecutionStart;
    private long lastExecutionStop;
    private long scheduledTime;
    private long timeoutInMinutes;
    private Thread thread;
    private MNls nls;
    private String info;
    private TimerTaskInterceptor intercepter;
    private MProperties logTrailConfig;
    private UUID uuid = UUID.randomUUID();
    private String username = null;

    public SchedulerJob(ITimerTask task) {
        if (ITracer.get().current() != null)
            logTrailConfig = ITracer.serialize(ITracer.get().current().context());
        setTask(task);
        if (task == null) setName("null");
        else if (task instanceof Named) setName(((Named) task).getName());
        else setName(MSystem.getClassName(task));
    }

    public SchedulerJob(String name, ITimerTask task) {
        setTask(task);
        setName(name);
    }

    /**
     * Call this method to fire ticks to the scheduler. If the time is come the scheduler will
     * execute the operation and set 'done' to true.
     *
     * @param forced
     */
    public void doTick(boolean forced) {

        if (!forced) {
            if (!isCanceled() && task instanceof MTimerTask && ((MTimerTask) task).isCanceled()) {
                log.i("doTick canceled 1", getName(), task);
                cancel();
            }
            if (isCanceled()) {
                log.i("doTick canceled 2", getName(), task);
                return;
            }

            if (getNextExecutionTime() == CALCULATE_NEXT) {
                synchronized (this) {
                    doCaclulateNextExecution();
                }
            }
        }

        if (forced || isExecutionTimeReached()) {

            Aaa.subjectCleanup();
            try (SubjectEnvironment access = Aaa.asSubject(username)) {
                SpanContext ctx = null;
                Scope scope = null;
                if (getLogTrailConfig() != null) ctx = ITracer.deserialize(getLogTrailConfig());
                if (ctx == null)
                    scope = ITracer.get().enter(getName());
                else {
                    Span span = ITracer.get()
                            .tracer()
                            .buildSpan(getName())
                            .addReference(References.FOLLOWS_FROM, ctx)
                            .start();
                    scope = ITracer.get().tracer().scopeManager().activate(span);
                }
                try (Scope scopeFinal = scope) {
                    boolean doIt = true;
                    if (intercepter != null) {
                        log.d("Intercepter beforeExecution", getName());
                        doIt = intercepter.beforeExecution(this, context, forced);
                    }
                    if (doIt) {
                        thread = Thread.currentThread();
                        lastExecutionStart = System.currentTimeMillis();
                        OperationResult res = null;
                        try {
                            if (!hasAccess(context)) {
                                log.d("access denied", context, context.getErrorMessage());
                                res =
                                        new NotSuccessful(
                                                this,
                                                "access denied",
                                                OperationResult.ACCESS_DENIED);
                            } else if (!canExecute(context)) {
                                log.d("execution denied", context.getErrorMessage());
                                res =
                                        new NotSuccessful(
                                                this,
                                                context.getErrorMessage() != null
                                                        ? context.getErrorMessage()
                                                        : "can't execute",
                                                OperationResult.NOT_EXECUTABLE);
                            } else res = doExecute(context);
                            log.d("Finished", res);
                        } catch (Throwable e) {
                            log.d("Error", getName(), e);
                            doError(e);
                            if (intercepter != null) intercepter.onError(this, context, e);
                        }
                        lastExecutionStop = System.currentTimeMillis();

                        thread = null;
                    }
                    synchronized (this) {
                        doCaclulateNextExecution();
                        log.d("Scheduled to", getName(), getNextExecutionTime());
                    }
                    if (doIt) {
                        if (intercepter != null) {
                            log.d("Intercepter afterExecution", getName());
                            intercepter.afterExecution(this, context);
                        }
                    }
                    context.clear();
                    setDone(true);
                }
            } catch (Throwable e) {
                // reshedule in case of an exception - this will prevent amok behavior
                synchronized (this) {
                    doCaclulateNextExecution();
                    log.d("Scheduled to", getName(), getNextExecutionTime());
                }
                throw e;
            }
        } else {
            log.d("Execution delayed", task);
        }
    }

    /** Calculate the next executionTime and store it into nextExecutionTime */
    protected abstract void doCaclulateNextExecution();

    @Override
    public final OperationResult doExecute(TaskContext context) throws Exception {
        log.d("execute", getClass(), context.getParameters());
        // must be done by caller
        //        if (!hasAccess(context)) {
        //            log.d("access denied", context, context.getErrorMessage());
        //            return new NotSuccessful(this, "access denied",
        // OperationResult.ACCESS_DENIED);
        //        }
        //        if (!canExecute(context)) {
        //            log.d("execution denied", context.getErrorMessage());
        //            return new NotSuccessful(
        //                    this,
        //                    context.getErrorMessage() != null ? context.getErrorMessage() : "can't
        // execute",
        //                    OperationResult.NOT_EXECUTABLE);
        //        }
        OperationResult ret = doExecute2(context);
        log.t("result", getClass(), ret);
        return ret;
    }

    protected OperationResult doExecute2(TaskContext context) throws Exception {
        try {
            if (task != null) task.run(context);
        } catch (LinkageError le) {
            log.d("cancel task because of fatal errors", this, le);
            cancel();
        }
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
            if (owner == null) {
                this.owner = null;
                return true;
            }
            if (this.owner != owner) return false;
            this.owner = null;
        }
        return true;
    }

    public Object getOwner() {
        return owner;
    }

    /**
     * By default the function will compare the nextExecutionTime with the current time. If the time
     * is come it will return true. If nextExecutionTime is 0 or less it will return false in every
     * case.
     *
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

        public MyTaskContext() {
            super(SchedulerJob.class);
        }

        public void clear() {
            errorMessage = null;
        }
    }

    @Override
    public boolean hasAccess(TaskContext context) {
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

    public ITimerTask getTask() {
        return task;
    }

    protected void setTask(ITimerTask task) {
        this.task = task;
    }

    @Override
    public void doit() throws Exception {
        doTick(false);
    }

    public long getNextExecutionTime() {
        return nextExecutionTime;
    }

    protected void doError(Throwable t) {
        log.e(getName(), t);
    }

    protected void doSchedule(Scheduler scheduler) {
        doReschedule(scheduler, getNextExecutionTime());
    }

    protected void doReschedule(Scheduler scheduler, long time) {
        setNextExecutionTime(time);
        if (isCanceled()) return;
        if (getNextExecutionTime() == CALCULATE_NEXT) doCaclulateNextExecution();
        if (getNextExecutionTime() == REMOVE_TIME) {
            return;
        }
        if (getNextExecutionTime() == DISABLED_TIME) {
            setScheduledTime(
                    System.currentTimeMillis() + MPeriod.DAY_IN_MILLISECOUNDS); // schedule tomorrow
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
        return getClass().getName();
    }

    public long getTimeoutInMinutes() {
        return timeoutInMinutes;
    }

    public void setTimeoutInMinutes(long timeoutInMinutes) {
        this.timeoutInMinutes = timeoutInMinutes;
    }

    /**
     * If you have defined a timeout, the event will be called every minute. Be carefully with the
     * execution time of this event, it can hem the hole scheduler.
     */
    public void doTimeoutReached() {}

    public Thread getThread() {
        return thread;
    }

    public void setNextExecutionTime(long nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    @Override
    public MNls getNls() {
        if (nls == null) nls = MNls.lookup(this);
        return nls;
    }

    @Override
    public String nls(String text) {
        return MNls.find(this, text);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public TimerTaskInterceptor getInterceptor() {
        return intercepter;
    }

    public void setIntercepter(TimerTaskInterceptor intercepter) {
        this.intercepter = intercepter;
        if (intercepter != null) intercepter.initialize(this);
    }

    public IProperties getLogTrailConfig() {
        return logTrailConfig;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }
}
