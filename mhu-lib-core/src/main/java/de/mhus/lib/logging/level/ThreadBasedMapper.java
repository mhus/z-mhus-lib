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

import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.logging.TrailLevelMapper;

public class ThreadBasedMapper implements TrailLevelMapper {
	
	private ThreadLocal<ThreadMapperConfig> map = new ThreadLocal<>();
	
	public void set(ThreadMapperConfig config) {
		map.set(config);
	}

	public void set(String config) {
		if (config == null || !config.startsWith(ThreadMapperConfig.MAP_LABEL)) return;
		ThreadMapperConfig c = new ThreadMapperConfig();
		c.doConfigure(config);
		set(c);
	}
	
	public void release() {
		map.remove();
	}
	
	public ThreadMapperConfig get() {
		ThreadMapperConfig config = map.get();
		if (config == null) return null;
		if (config.isTimedOut()) {
			release();
			return null;
		}
		return config;
	}
	
	@Override
	public LEVEL map(Log log, LEVEL level, Object... msg) {
		ThreadMapperConfig config = get();
		if (config == null) return level;
		return config.map(log, level, msg);
	}

	@Override
	public String doSerializeTrail() {
		ThreadMapperConfig c = get();
		return c == null ? null : c.doSerialize();
	}

	@Override
	public void doConfigureTrail(String config) {
		//if (backup == null) return;
		if (MString.isEmpty(config)) config = ThreadMapperConfig.MAP_LABEL;
		set(config);
	}

	@Override
	public void doResetTrail() {
		release();
	}

	@Override
	public boolean isLocalTrail() {
		ThreadMapperConfig c = get();
		return c == null ? false : c.isLocal();
	}

	@Override
	public void prepareMessage(Log log, StringBuilder msg) {
		ThreadMapperConfig config = map.get();
		if (config == null) {
    		msg.append('(').append(Thread.currentThread().getId()).append(')');
		} else {
			config.prepareMessage(log, msg);
		}
	}

	@Override
	public String getTrailId() {
		ThreadMapperConfig config = map.get();
		if (config == null) {
    		return String.valueOf(Thread.currentThread().getId());
		} else {
			return config.getTrailId();
		}
	}

	@Override
	public String toString() {
		return MSystem.toString(this, map.get());
	}

	@Override
	public void doResetAllTrails() {
		map = new ThreadLocal<>();
	}

}
