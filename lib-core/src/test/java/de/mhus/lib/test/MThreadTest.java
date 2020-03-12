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
package de.mhus.lib.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MThread;
import de.mhus.lib.core.MThreadDaemon;
import de.mhus.lib.core.logging.Log.LEVEL;

public class MThreadTest {

    protected boolean done;

    @Test
    public void testThread() throws Exception {

        System.out.println(">>> Thread");
        done = false;
        MThread t =
                new MThread(
                                new Runnable() {

                                    @Override
                                    public void run() {
                                        System.out.println("Started");
                                        done = true;
                                    }
                                })
                        .start();
        System.out.println(t);
        assertNotNull(t.getThread());
        MThread.waitForWithException(() -> done, 5000);
    }

    @Test
    public void testThreadDaemon() throws Exception {

        System.out.println(">>> ThreadDaemon");
        done = false;
        MThread t =
                new MThreadDaemon(
                                new Runnable() {

                                    @Override
                                    public void run() {
                                        System.out.println("Started");
                                        done = true;
                                    }
                                })
                        .start();
        System.out.println(t);
        assertNotNull(t.getThread());
        assertEquals(true, t.getThread().isDaemon());
        MThread.waitForWithException(() -> done, 5000);
    }

    @Test
    public void testThreadDirect() throws Exception {

        System.out.println(">>> Thread Direct");
        done = false;
        MThread t =
                new MThread() {
                    @Override
                    public void run() {
                        System.out.println("Started");
                        done = true;
                    }
                }.start();
        System.out.println(t);
        assertNotNull(t.getThread());
        MThread.waitForWithException(() -> done, 5000);
    }

    @Test
    public void testThreadException() throws Exception {

        System.out.println(">>> Thread Exception");
        done = false;
        MThread t =
                new MThread() {
                    @Override
                    public void run() {
                        System.out.println("Started");
                        throw new RuntimeException("Peng");
                    }

                    @Override
                    protected void taskError(Throwable t) {
                        System.out.println(t.getMessage());
                        done = true;
                    }
                }.start();
        System.out.println(t);
        assertNotNull(t.getThread());
        MThread.waitForWithException(() -> done, 5000);
    }

    @BeforeAll
    public static void setUp() throws Exception {
        MApi.get().getLogFactory().setDefaultLevel(LEVEL.TRACE);
    }
}
