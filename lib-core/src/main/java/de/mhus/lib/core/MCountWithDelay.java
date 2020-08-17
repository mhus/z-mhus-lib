/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
package de.mhus.lib.core;

import de.mhus.lib.annotations.jmx.JmxManaged;

@JmxManaged(descrition = "Counter with delay")
public class MCountWithDelay extends MCount {

    private long sleepInterval = 0;
    private int sleepSeconds = 0;
    private boolean throwExceptionOnNextCount = false;

    public MCountWithDelay() {
        super();
    }

    public MCountWithDelay(String name) {
        super(name);
    }

    @JmxManaged(descrition = "Get the interval")
    public long getSleepInterval() {
        return sleepInterval;
    }

    @JmxManaged(descrition = "Set the interval after it will sleeping")
    public void setSleepInterval(long sleepInterval) {
        this.sleepInterval = sleepInterval;
    }

    @JmxManaged(descrition = "Get the seconds to sleep each interval")
    public int getSleepSeconds() {
        return sleepSeconds;
    }

    @JmxManaged(descrition = "Set the seconds to sleep each interval")
    public void setSleepSeconds(int sleepSeconds) {
        this.sleepSeconds = sleepSeconds;
    }

    @Override
    public void inc() {
        super.inc();
        if (throwExceptionOnNextCount) {
            throwExceptionOnNextCount = false;
            throw new RuntimeException(
                    "Counter " + getName() + " is thrown by request at " + getValue());
        }
        if (isClosed) return;
        if (sleepInterval > 0 && sleepSeconds > 0 && cnt % sleepInterval == 0) {
            log().d(getName(), "Sleep", sleepSeconds);
            MThread.sleep(sleepSeconds * 1000);
        }
    }

    @JmxManaged(
            descrition = "Should the counter create and throw a RunntimeException() on next count")
    public boolean isThrowExceptionOnNextCount() {
        return throwExceptionOnNextCount;
    }

    @JmxManaged(
            descrition = "Should the counter create and throw a RunntimeException() on next count")
    public void setThrowExceptionOnNextCount(boolean throwExceptionOnNextCount) {
        this.throwExceptionOnNextCount = throwExceptionOnNextCount;
    }
}
