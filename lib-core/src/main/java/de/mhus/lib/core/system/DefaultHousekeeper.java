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
package de.mhus.lib.core.system;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.WeakHashMap;

import de.mhus.lib.basics.ActivatorObjectLifecycle;
import de.mhus.lib.core.MHousekeeper;
import de.mhus.lib.core.MHousekeeperTask;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MTimer;
import de.mhus.lib.core.MTimerTask;

public class DefaultHousekeeper extends MLog implements MHousekeeper, ActivatorObjectLifecycle {

    private MTimer timer;
    private static WeakHashMap<MHousekeeperTask, Long> list = new WeakHashMap<>();

    public static void put(MHousekeeperTask task, long sleep) {
        // TODO check access
        list.put(task, sleep);
    }

    public static Map<MHousekeeperTask, Long> getAll() {
        return new HashMap<>(list);
    }

    public DefaultHousekeeper() {
        log().t("new default housekeeper");
        timer = new MTimer(true);
    }

    @Override
    public void register(MHousekeeperTask task, long sleep) {
        put(task, sleep);
        timer.schedule(new MyTimerTask(task), sleep, sleep);
    }

    @Override
    public void finalize() {
        log().t("finalize");
        if (timer != null) timer.cancel();
        timer = null;
    }

    private class MyTimerTask extends MTimerTask {

        private WeakReference<TimerTask> refWeak;
        private TimerTask ref;

        public MyTimerTask(TimerTask task) {
            refWeak = new WeakReference<TimerTask>(task);
        }

        @Override
        public void doit() throws Exception {

            TimerTask r = null;

            if (refWeak != null) r = refWeak.get();
            else r = ref;

            if (r == null) {
                this.cancel();
                return;
            }
            if (r instanceof MTimerTask) {
                if (((MTimerTask) r).isCanceled()) {
                    this.cancel();
                    return;
                }
            }

            r.run();
        }
    }

    @Override
    public void objectActivated(String ifcName, Object currentObject) {}

    @Override
    public void objectDeactivated() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
