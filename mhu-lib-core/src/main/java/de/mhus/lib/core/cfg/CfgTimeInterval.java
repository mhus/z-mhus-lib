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
package de.mhus.lib.core.cfg;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.directory.ResourceNode;

public class CfgTimeInterval extends CfgValue<String>{

	private long interval;

	public CfgTimeInterval(Object owner, String path, String def) {
		super(owner, path, def);
	}

	@Override
	protected String loadValue() {
		int p = getPath().indexOf('@');
		if (p < 0) 
			return MApi.getCfg(getOwner()).getString(getPath(), getDefault());
		ResourceNode<?> node = MApi.getCfg(getOwner()).getNodeByPath(getPath().substring(0, p));
		if (node == null) return getDefault();
		return node.getString(getPath().substring(p+1), getDefault());
	}

	@Override
	protected String loadValue(String value) {
		return value;
	}

	@Override
	void update() {
		String newValue = loadValue();
		interval = MTimeInterval.toTime(newValue, -1);
		if (interval == -1)
			interval = MTimeInterval.toTime(getDefault(), -1);
		super.update();
	}

	public long interval() {
		return interval;
	}
}
