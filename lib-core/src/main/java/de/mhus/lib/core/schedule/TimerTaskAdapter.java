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
package de.mhus.lib.core.schedule;

import java.util.TimerTask;

import de.mhus.lib.basics.Named;
import de.mhus.lib.core.ITimerTask;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MTimerTask;

public class TimerTaskAdapter extends MTimerTask {

    private TimerTask task;

    public TimerTaskAdapter(TimerTask task) {
        this(null, task);
    }

    public TimerTaskAdapter(String name, TimerTask task) {
        this.task = task;
        if (name == null) name = MSystem.getClassName(task);
        setName(name);
        if (task != null && task instanceof Named) setName(((Named) task).getName());
    }

    @Override
    public String toString() {
        return task == null ? "null" : task.toString();
    }

    @Override
    public void doit() throws Exception {
        if (task instanceof ITimerTask && ((ITimerTask) task).isCanceled()) {
            cancel();
            return;
        }

        task.run();
    }

    public TimerTask getTask() {
        return task;
    }
}
