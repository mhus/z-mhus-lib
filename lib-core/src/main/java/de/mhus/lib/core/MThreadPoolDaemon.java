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

import java.util.Vector;
import java.util.function.Consumer;

/**
 * @author hummel
 *     <p>To change the template for this generated type comment go to
 *     Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MThreadPoolDaemon extends MThreadPool implements Runnable {

    public MThreadPoolDaemon() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MThreadPoolDaemon(Runnable _task, String _name) {
        super(_task, _name);
        // TODO Auto-generated constructor stub
    }

    public MThreadPoolDaemon(Runnable _task) {
        super(_task);
        // TODO Auto-generated constructor stub
    }

    public MThreadPoolDaemon(String _name) {
        super(_name);
        // TODO Auto-generated constructor stub
    }

    private static Vector<ThreadContainer> pool = new Vector<ThreadContainer>();
    private static ThreadGroup group = new ThreadGroup("MThreadPoolDeamon");

    @Override
    public MThreadPoolDaemon start() {
        tc = start(this, name);
        return this;
    }

    private static ThreadContainer start(MThreadPoolDaemon _task, String _name) {

        // search free thread
        ThreadContainer tc = null;
        synchronized (pool) {
            for (int i = 0; i < pool.size(); i++)
                if (!pool.elementAt(i).isWorking()) {
                    tc = pool.elementAt(i);
                    break;
                }

            if (tc == null) {
                tc = new ThreadContainer(group, "AT_" + pool.size());
                tc.setDaemon(true);
                tc.start();
                pool.addElement(tc);
            }

            log.t("###: NEW THREAD", tc.getId());
            tc.setName(_name);
            tc.newWork(_task);
        }
        return tc;
    }

    public static void poolClean(long pendingTime) {
        synchronized (pool) {
            ThreadContainer[] list = pool.toArray(new ThreadContainer[pool.size()]);
            for (int i = 0; i < list.length; i++) {
                long sleep = list[i].getSleepTime();
                if (sleep != 0 && sleep <= pendingTime) {
                    pool.remove(list[i]);
                    list[i].stopRunning();
                }
            }
        }
    }

    public static void poolClean() {

        synchronized (pool) {
            ThreadContainer[] list = pool.toArray(new ThreadContainer[pool.size()]);
            for (int i = 0; i < list.length; i++) {
                if (!list[i].isWorking()) {
                    pool.remove(list[i]);
                    list[i].stopRunning();
                }
            }
        }
    }

    public static int poolSize() {
        synchronized (pool) {
            return pool.size();
        }
    }

    public static int poolWorkingSize() {
        int size = 0;
        synchronized (pool) {
            ThreadContainer[] list = pool.toArray(new ThreadContainer[pool.size()]);
            for (int i = 0; i < list.length; i++) {
                if (list[i].isWorking()) size++;
            }
        }
        return size;
    }

    public static void run(Runnable task) {
        new MThreadPoolDaemon(
                        new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    task.run();
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                }
                            }
                        })
                .start();
    }

    public static void run(Consumer<Thread> consumer) {
        new MThreadPoolDaemon(
                        new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    consumer.accept(Thread.currentThread());
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                }
                            }
                        })
                .start();
    }
}
