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
package de.mhus.lib.core.jmx;

import java.util.HashSet;

public class JmxPackage extends JmxObject {

	private HashSet<JmxObject> registry = new HashSet<JmxObject>();
	private MRemoteManager remoteManager;
	
	public void open(MRemoteManager remoteManager) {
		this.remoteManager = remoteManager;
		for (JmxObject bo : registry)
			try {
				remoteManager.register(bo);
			} catch (Exception e) {}
	}

	public void close() {
		for (JmxObject bo : registry)
			try {
				remoteManager.unregister(bo);
			} catch (Exception e) {}		
		remoteManager = null;
	}

	public void register(JmxObject object) throws Exception {
		object.setJmxPackage(getJmxPackage());
		if (remoteManager!= null)
			remoteManager.register(object);
		registry.add(object);
	}

	public void unregister(JmxObject object) throws Exception {
		if (remoteManager!= null)
			remoteManager.unregister(object);
		registry.remove(object);
	}
	
}
