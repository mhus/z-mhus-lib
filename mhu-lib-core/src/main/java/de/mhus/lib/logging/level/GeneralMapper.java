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
package de.mhus.lib.logging.level;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.Log.LEVEL;

public class GeneralMapper implements LevelMapper {

	private ThreadMapperConfig config;
	
	@Override
	public LEVEL map(Log log, LEVEL level, Object... msg) {
		if (config == null) return level;
		return config.map(log, level, msg);
	}

	@Override
	public void prepareMessage(Log log, StringBuilder msg) {
		msg.append('(').append(Thread.currentThread().getId()).append(')');
	}

	public ThreadMapperConfig getConfig() {
		return config;
	}

	public void setConfig(ThreadMapperConfig config) {
		this.config = config;
	}

	@Override
	public String toString() {
		return MSystem.toString(this, config);
	}
	
}
