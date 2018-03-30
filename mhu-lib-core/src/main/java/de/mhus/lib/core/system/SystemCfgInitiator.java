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
package de.mhus.lib.core.system;

import java.io.File;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.directory.ResourceNode;

public class SystemCfgInitiator implements CfgInitiator {

	@Override
	public void doInitialize(IApiInternal internal, CfgManager manager, IConfig config) {
		ResourceNode<?> system = manager.getCfg("system");
		try {
			String key = MConstants.PROP_BASE_DIR;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			String baseDirName = ".";
			if (MString.isSet(name)) 
				baseDirName = name;
			internal.setBaseDir( new File(baseDirName) );
		} catch (Throwable t) {MApi.dirtyLog(t);}
	}

}
