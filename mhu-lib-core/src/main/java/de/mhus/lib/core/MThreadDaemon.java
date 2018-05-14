/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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

	private static ThreadGroup group = new ThreadGroup("AThreadDaemon");

	@Override
	public MThreadDaemon start() {
		tc = start(this, name);
		return this;
	}

	private static ThreadContainer start(MThreadDaemon _task, String _name) {

		ThreadContainer tc = null;
		tc = new ThreadContainer(group, "AT_" + _name, true);
		tc.setDaemon(true);
		tc.start();

		log.t("###: NEW THREAD",tc.getId());
		tc.setName(_name);
		tc.newWork(_task);

		return tc;
	}

	public static void poolClean(long pendingTime) {
	}

	public static void poolClean() {
	}

	public static int poolSize() {
		return 0;
	}

	public static int poolWorkingSize() {
		return 0;
	}

}
