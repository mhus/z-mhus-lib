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
        if (isDone()) setNextExecutionTime(REMOVE_TIME);
        else setNextExecutionTime(time);
    }

    @Override
    public void setDone(boolean done) {
        super.setDone(done);
        if (done) cancel();
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
