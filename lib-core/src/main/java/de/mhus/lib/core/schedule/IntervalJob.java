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

import de.mhus.lib.core.ITimerTask;
import de.mhus.lib.core.MCast;

/**
 * Executes the task every 'interval' milliseconds after execution finished.
 *
 * @author mikehummel
 */
public class IntervalJob extends SchedulerJob implements MutableSchedulerJob {

    private long interval;

    public IntervalJob(long interval, ITimerTask task) {
        super(task);
        this.interval = interval;
    }

    @Override
    public void doCaclulateNextExecution() {
        setNextExecutionTime(System.currentTimeMillis() + interval);
    }

    @Override
    public String toString() {
        return IntervalJob.class.getSimpleName() + "," + interval;
    }

    @Override
    public void doReschedule(Scheduler queue, long time) {
        super.doReschedule(queue, time);
    }

    @Override
    public void setDone(boolean done) {
        super.setDone(done);
    }

    @Override
    public boolean doReconfigure(String config) {
        long l = MCast.tolong(config, -1);
        if (l > 0) {
            interval = l;
            return true;
        }
        return false;
    }

    @Override
    public void setScheduledTime(long scheduledTime) {
        super.setScheduledTime(scheduledTime);
    }

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
    }
}
