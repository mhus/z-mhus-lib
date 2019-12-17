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

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogFactory;

public interface IApi {

	//Log createLog(Object owner);

	CfgManager getCfgManager();

	BaseControl getBaseControl();

	MActivator createActivator();

	LogFactory getLogFactory();
	
	boolean isTrace(String name);
	
	Base base();

	/**
	 * Return a File inside the current application context.
	 * 
	 * @param scope Where to locate the requested file name.
	 * @param name The name / path of the file or directory inside the scope
	 * @return file The file.
	 */
	File getFile(MApi.SCOPE scope, String name);

	default String getSystemProperty(String name, String def) {
        String value = System.getProperty(name);
        if (value == null) {
            switch (name) {
            case MConstants.PROP_CONFIG_FILE: {
                String file = MConstants.DEFAULT_MHUS_CONFIG_FILE;
                return getFile(MApi.SCOPE.ETC, file).getAbsolutePath();
            }
            case MConstants.PROP_TIMER_CONFIG_FILE: {
                String file = MConstants.DEFAULT_MHUS_TIMER_CONFIG_FILE;
                file = getCfgString(IApi.class, MConstants.PROP_TIMER_CONFIG_FILE, file);
                return getFile(MApi.SCOPE.ETC, MConstants.DEFAULT_MHUS_TIMER_CONFIG_FILE).getAbsolutePath();
            }
            default:
                return def;
            }
        }
        return value;
	}

	Log lookupLog(Object owner);

	void updateLog();

	default String getCfgString(Class<?> owner, String path, String def) {
		int p = path.indexOf('@');
		if (p < 0) 
			return MApi.getCfg(owner).getString(path, def);
		ResourceNode<?> node = MApi.getCfg(owner).getNodeByPath(path.substring(0, p));
		if (node == null) return def;
		return node.getString(path.substring(p+1), def);		
	}
		
}
