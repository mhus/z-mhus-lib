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
	 * @param dir
	 * @return
	 */
	File getFile(String dir);

	String getSystemProperty(String name, String def);

	Log lookupLog(Object owner);

	void updateLog();
}
