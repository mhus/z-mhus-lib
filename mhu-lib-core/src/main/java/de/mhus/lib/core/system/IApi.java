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

	enum SCOPE {LOG,TMP,ETC,DEPLOY,DATA}
	/**
	 * Return a File inside the current application context.
	 * 
	 * @param scope Where to locate the requested file name.
	 * @param name The name / path of the file or directory inside the scope
	 * @return file The file.
	 */
	File getFile(SCOPE scope, String name);

	String getSystemProperty(String name, String def);

	Log lookupLog(Object owner);

	void updateLog();
}
