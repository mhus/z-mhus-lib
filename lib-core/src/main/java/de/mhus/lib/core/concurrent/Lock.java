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
package de.mhus.lib.core.concurrent;

import java.io.Closeable;

import de.mhus.lib.core.logging.ITracer;
import de.mhus.lib.errors.TimeoutException;
import de.mhus.lib.errors.TimeoutRuntimeException;
import io.opentracing.Scope;

public interface Lock extends Closeable {

    Lock lock();

    default Lock lockWithException(long timeout) throws TimeoutException {
        if (lock(timeout)) return this;
        throw new TimeoutException(getName());
    }

    boolean lock(long timeout);

    /**
     * Run the given task in a locked environment.
     *
     * @param task
     */
    default void lock(Runnable task) {
        try (Lock lock = lock()) {
            task.run();
        }
    }

    /**
     * Run the given task in a locked environment.
     *
     * @param task
     * @param timeout
     * @throws TimeoutException
     */
    default void lock(Runnable task, long timeout) throws TimeoutException {
        try (Lock lock = lockWithException(timeout)) {
            task.run();
        }
    }

    /**
     * Unlock if the current thread is also the owner.
     *
     * @return true if unlock was successful
     */
    boolean unlock();

    /**
     * A synonym to unlock()
     *
     * @return true if unlock was successful
     */
    default boolean release() {
        return unlock();
    }

    /** Unlock in every case !!! This can break a locked area. */
    void unlockHard();

    /** Sleeps until the lock is released. */
    default void waitUntilUnlock() {
        synchronized (this) {
            while (isLocked()) {
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }
        }
    }

    default void waitWithException(long timeout) {
        if (waitUntilUnlock(timeout)) return;
        throw new TimeoutRuntimeException(getName());
    }

    @SuppressWarnings("resource")
    default boolean waitUntilUnlock(long timeout) {
        Scope scope = null;
        try {
            synchronized (this) {
                while (isLocked()) {
                    long start = System.currentTimeMillis();
                    if (scope != null)
                        scope = ITracer.get().enter("waitUntilUnlock", "name", getName());
                    try {
                        wait(timeout);
                    } catch (InterruptedException e) {
                    }
                    if (System.currentTimeMillis() - start >= timeout) return false;
                }
                return true;
            }
        } finally {
            if (scope != null) scope.close();
        }
    }

    boolean isLocked();

    String getName();

    String getOwner();

    long getLockTime();

    @Override
    default void close() {
        unlock();
    }

    /**
     * Some locks need to be refreshed.
     *
     * @return True if locked
     */
    boolean refresh();

    long getCnt();

    String getStartStackTrace();
}
