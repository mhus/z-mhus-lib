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

/**
 * @author hummel
 *     <p>To change the template for this generated type comment go to
 *     Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MThreadDaemon extends MThread implements Runnable {

    public MThreadDaemon() {
        super();
    }

    public MThreadDaemon(Runnable _task, String _name) {
        super(_task, _name);
    }

    public MThreadDaemon(Runnable _task) {
        super(_task);
    }

    public MThreadDaemon(String _name) {
        super(_name);
    }

    private static ThreadGroup daemonGroup = new ThreadGroup("MThreadDaemon");

    @Override
    protected void initThread(Thread thread) {
        super.initThread(thread);
        thread.setDaemon(true);
    }

    @Override
    protected ThreadGroup getGroup() {
        return daemonGroup;
    }
}
