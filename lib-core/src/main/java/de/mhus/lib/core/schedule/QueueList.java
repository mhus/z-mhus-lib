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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class QueueList implements SchedulerQueue {

    private LinkedList<SchedulerJob> list = new LinkedList<>();

    @Override
    public List<SchedulerJob> removeJobs(long toTime) {
        LinkedList<SchedulerJob> out = null;
        synchronized (this) {
            while (true) {
                if (list.size() == 0) break;
                SchedulerJob first = list.getFirst();
                if (first.getScheduledTime() <= toTime) {
                    if (out == null) out = new LinkedList<>();
                    out.add(first);
                    list.removeFirst();
                } else {
                    break;
                }
            }
        }
        return out;
    }

    @Override
    public void doSchedule(SchedulerJob schedulerJob) {
        long time = schedulerJob.getScheduledTime();
        if (time <= 0) return;
        synchronized (this) {
            Iterator<SchedulerJob> iter = list.iterator();
            int pos = 0;
            while (iter.hasNext()) {
                SchedulerJob item = iter.next();
                if (item.getScheduledTime() >= time) {
                    list.add(pos, schedulerJob);
                    return;
                }
                pos++;
            }
            list.add(schedulerJob);
        }
    }

    @Override
    public void removeJob(SchedulerJob job) {
        synchronized (this) {
            Iterator<SchedulerJob> iter = list.iterator();
            while (iter.hasNext()) {
                SchedulerJob item = iter.next();
                if (job.equals(item)) iter.remove();
            }
        }
    }

    @Override
    public int size() {
        synchronized (this) {
            return list.size();
        }
    }

    @Override
    public List<SchedulerJob> getJobs() {
        synchronized (this) {
            return new LinkedList<SchedulerJob>(list);
        }
    }

    @Override
    public boolean contains(SchedulerJob job) {
        synchronized (this) {
            return list.contains(job);
        }
    }

    @Override
    public void clear() {
        synchronized (this) {
            list.clear();
        }
    }
}
