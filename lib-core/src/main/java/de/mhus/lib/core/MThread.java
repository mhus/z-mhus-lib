/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core;

import de.mhus.lib.basics.Named;
import de.mhus.lib.core.lang.Checker;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.core.lang.Value;
import de.mhus.lib.core.lang.ValueProvider;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.errors.RuntimeInterruptedException;
import de.mhus.lib.errors.TimeoutRuntimeException;

/**
 * @author hummel
 *     <p>To change the template for this generated type comment go to
 *     Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MThread extends MObject implements Runnable {

    protected static Log log = Log.getLog(MThread.class);

    protected Runnable task = this;
    protected String name = null;
    protected Thread thread = null;

    private int priority = -1;

    public MThread() {}

    public MThread(String _name) {
        name = _name;
    }

    public MThread(Runnable _task) {
        task = _task;
    }

    public MThread(Runnable _task, String _name) {
        task = _task;
        name = _name;
    }

    protected Runnable getTask() {
        return task;
    }

    @Override
    public void run() {}

    public MThread start() {
        synchronized (this) {
            if (thread != null) throw new IllegalThreadStateException();
            Container container = new Container();
            thread = new Thread(getGroup(), container);
            initThread(thread);
            thread.start();
        }
        return this;
    }

    private static ThreadGroup group = new ThreadGroup("MThread");

    protected ThreadGroup getGroup() {
        return group;
    }

    protected void initThread(Thread thread) {
        if (priority != -1) thread.setPriority(priority);

        if (name == null) {
            if (task == null) name = "null";
            else if (task instanceof Named) name = "MThread " + ((Named) task).getName();
            else name = "MThread " + MSystem.getCanonicalClassName(task.getClass());
        }

        thread.setName(name);
    }

    private class Container implements Runnable {

        private long parentThreadId;
        private String trailConfig;

        public Container() {
            try {
                parentThreadId = Thread.currentThread().getId();
                trailConfig = MLogUtil.getTrailConfig();
            } catch (Throwable t) {
            }
        }

        @Override
        public void run() {
            try {
                if (trailConfig != null) MLogUtil.setTrailConfig(trailConfig);
                log().t("###: NEW THREAD", parentThreadId, thread.getId());
            } catch (Throwable t) {
            }
            try {
                if (task != null) task.run();
            } catch (Throwable t) {
                taskError(t);
            }
            try {
                log.t("###: LEAVE THREAD", thread.getId());
                MLogUtil.releaseTrailConfig();
            } catch (Throwable t) {
            }
        }
    }

    public void setName(String _name) {
        this.name = _name;
        if (thread != null) thread.setName(_name);
    }

    public String getName() {
        return name;
    }

    public void setPriority(int _p) {
        this.priority = _p;
        if (thread != null) thread.setPriority(_p);
    }

    public int getPriority() {
        return priority;
    }

    @SuppressWarnings("deprecation")
    public void stop() {
        if (thread == null) return;
        thread.stop();
    }

    public void interupt() {
        if (thread == null) return;
        thread.interrupt();
    }

    @Override
    public String toString() {
        if (thread != null)
            return MSystem.toString(
                    "MThread", name, thread.getId(), thread.getPriority(), thread.getState());
        else return MSystem.toString("MThread", name);
    }

    /**
     * Sleeps _millisec milliseconds. On Interruption it will throw an RuntimeInterruptedException
     *
     * @param _millisec
     */
    public static void sleep(long _millisec) {
        try {
            Thread.sleep(_millisec);
        } catch (InterruptedException e) {
            throw new RuntimeInterruptedException(e);
        }
    }

    /**
     * Sleeps _millisec milliseconds. On Interruption it will throw an InterruptedException.
     * If thread is already interrupted, it will throw the exception directly.
     *
     * This can be used in loops if a interrupt should be able to stop the loop.
     *
     * @param _millisec
     * @throws InterruptedException on interrupt
     */
    public static void sleepInLoop(long _millisec) throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        Thread.sleep(_millisec);
    }
    
    /**
     * Sleeps _millisec milliseconds. On Interruption it will print a debug stack trace but not break.
     * It will leave the Thread.interrupted state to false
     * see https://docs.oracle.com/javase/tutorial/essential/concurrency/interrupt.html
     * 
     * @param _millisec
     * @return true if the thread was interrupted in the sleep time
     */
    public static boolean sleepForSure(long _millisec) {
        boolean interrupted = false;
        while (true) {
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(_millisec);
                return interrupted;
            } catch (InterruptedException e) {
                interrupted = true;
                try {
                    Thread.sleep(1); // clear interrupted state
                } catch (InterruptedException e1) {
                } 
                log.d(e);
                long done = System.currentTimeMillis() - start;
                _millisec = _millisec - done;
                if (_millisec <= 0) return interrupted;
            }
        }
    }
    
    protected void taskError(Throwable t) {
        log().e(name, t);
    }

    public static void asynchron(Runnable task) {
        new MThread(task).start();
    }

    /**
     * Try every 200ms to get the value. If the provider throws an error or return null the try will
     * be repeated. If the time out is reached a TimeoutRuntimeException will be thrown.
     *
     * @param provider
     * @param timeout
     * @param nullAllowed
     * @return The requested value
     */
    public static <T> T getWithTimeout(
            final ValueProvider<T> provider, long timeout, boolean nullAllowed) {
        long start = System.currentTimeMillis();
        while (true) {
            try {
                T val = provider.getValue();
                if (nullAllowed || val != null) return val;
            } catch (Throwable t) {
            }
            if (System.currentTimeMillis() - start > timeout) throw new TimeoutRuntimeException();
            sleep(200);
        }
    }

    /**
     * Like getWithTimeout but executed in a separate task, this means unblocking.
     *
     * @param provider
     * @param timeout
     * @param nullAllowed
     * @return The requested value
     */
    public static <T> T getAsynchronWithTimeout(
            final ValueProvider<T> provider, long timeout, boolean nullAllowed) {
        long start = System.currentTimeMillis();
        final Value<T> value = new Value<>();
        MThreadPool t =
                new MThreadPool(
                        new Runnable() {

                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        T val = provider.getValue();
                                        if (nullAllowed || val != null) {
                                            value.value = val;
                                            return;
                                        }
                                    } catch (Throwable t) {
                                    }
                                    if (System.currentTimeMillis() - start > timeout)
                                        throw new TimeoutRuntimeException();
                                    sleep(200);
                                }
                            }
                        });
        t.start();
        while (t.isAlive()) {
            if (System.currentTimeMillis() - start > timeout) throw new TimeoutRuntimeException();
            sleep(200);
        }
        return value.value;
    }

    /**
     * Calls the provider once and will return the result. The provider is called in a separate
     * thread to
     *
     * @param provider
     * @param timeout
     * @return The requested value
     * @throws Exception
     */
    public static <T> T getAsynchronWithTimeout(final ValueProvider<T> provider, long timeout)
            throws Exception {
        long start = System.currentTimeMillis();
        final Value<T> value = new Value<>();
        final Value<Throwable> error = new Value<>();
        MThreadPool t =
                new MThreadPool(
                        new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    value.value = provider.getValue();
                                } catch (Throwable t) {
                                    error.value = t;
                                }
                            }
                        });
        t.start();
        while (t.isAlive()) {
            if (System.currentTimeMillis() - start > timeout) throw new TimeoutRuntimeException();
            sleep(200);
        }
        if (error.value != null) {
            if (error.value instanceof RuntimeException) throw (RuntimeException) error.value;
            if (error.value instanceof Exception) throw (Exception) error.value;
            throw new Exception(error.value);
        }
        return value.value;
    }

    /**
     * Wait for the checker to return true or throw an TimeoutRuntimeException on timeout. A
     * exception in the checker will be ignored.
     *
     * @param checker
     * @param timeout
     */
    public static void waitFor(final Checker checker, long timeout) {
        long start = System.currentTimeMillis();
        while (true) {
            try {
                if (checker.check()) return;
            } catch (Throwable t) {
            }
            if (System.currentTimeMillis() - start > timeout) throw new TimeoutRuntimeException();
            sleep(200);
        }
    }

    /**
     * Wait for the checker to return true or throw an TimeoutRuntimeException on timeout.
     *
     * @param checker
     * @param timeout
     * @throws Exception Thrown if checker throws an exception
     */
    public static void waitForWithException(final Checker checker, long timeout) throws Exception {
        long start = System.currentTimeMillis();
        while (true) {
            try {
                if (checker.check()) return;
            } catch (Throwable t) {
                throw t;
            }
            if (System.currentTimeMillis() - start > timeout) throw new TimeoutRuntimeException();
            sleep(200);
        }
    }

    public Thread getThread() {
        return thread;
    }

    /**
     * Check if the thread was interrupted an throws the InterruptedException
     * exception.
     * @throws InterruptedException Throw if the thread was interrupted in the meantime.
     */
    public static void checkInterruptedException() throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
    }
}
