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
package de.mhus.lib.core.base.service;

import java.util.Date;
import java.util.TimerTask;

import de.mhus.lib.annotations.activator.DefaultFactory;
import de.mhus.lib.core.schedule.SchedulerJob;
import de.mhus.lib.core.util.DefaultTimerFactory;

@DefaultFactory(DefaultTimerFactory.class)
public interface TimerIfc {

    public void schedule(SchedulerJob job);

    public void schedule(TimerTask task, long delay);

    public void schedule(TimerTask task, Date time);

    public void schedule(TimerTask task, long delay, long period);

    public void schedule(TimerTask task, Date firstTime, long period);

    public void scheduleAtFixedRate(TimerTask task, long delay, long period);

    public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period);

    public void schedule(String name, TimerTask task, long delay);

    public void schedule(String name, TimerTask task, Date time);

    public void schedule(String name, TimerTask task, long delay, long period);

    public void schedule(String name, TimerTask task, Date firstTime, long period);

    public void scheduleAtFixedRate(String name, TimerTask task, long delay, long period);

    public void scheduleAtFixedRate(String name, TimerTask task, Date firstTime, long period);

    public void cancel();
}
