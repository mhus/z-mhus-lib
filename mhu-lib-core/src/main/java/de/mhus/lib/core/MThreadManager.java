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

import de.mhus.lib.core.MThread.ThreadContainer;
import de.mhus.lib.core.lang.IBase;
import de.mhus.lib.core.lang.MObject;

public class MThreadManager extends MObject implements IBase {

	private ThreadGroup group = new ThreadGroup("AThread");

	ThreadContainer start(MThread _task, String _name) {

		ThreadContainer tc = null;
		
		tc = new ThreadContainer(group, "AT " + _name, true);
		tc.start();

		log().t("###: NEW THREAD",tc.getId());
		tc.setName(_name);
		tc.newWork(_task);

		return tc;
	}

	public void poolClean(long pendingTime) {
		// only a skeleton since the thread pool is a facade
	}

	public void poolClean() {
		// only a skeleton since the thread pool is a facade
	}

	public int poolSize() {
		// only a skeleton since the thread pool is a facade
		return 0;
	}

	public int poolWorkingSize() {
		// only a skeleton since the thread pool is a facade
		return 0;
	}
	
}
