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
import java.util.HashSet;
import java.util.Set;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.M;
import de.mhus.lib.core.MApi.SCOPE;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.activator.DefaultActivator;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.logging.MLogFactory;

public class DefaultMApi implements IApi, ApiInitialize, IApiInternal {
	
	private LogFactory logFactory = new ConsoleFactory();
	private BaseControl baseControl;
	private CfgManager configProvider;
	private HashSet<String> logTrace = new HashSet<>();
	private File baseDir = new File(".");
	private MLogFactory mlogFactory;

	@Override
	public void doInitialize(ClassLoader coreLoader) {
		getCfgManager().reConfigure();
	}
		
	@Override
	public synchronized BaseControl getBaseControl() {
		if (baseControl == null) {
			baseControl = new BaseControl();
		}
		return baseControl;
	}

	@Override
	public MActivator createActivator() {
		return new DefaultActivator();
	}

	@Override
	public LogFactory getLogFactory() {
		return logFactory;
	}

	@Override
	public synchronized CfgManager getCfgManager() {
		if (configProvider == null) {
			configProvider = new CfgManager(this);
		}
		return configProvider;
	}

	@Override
	public boolean isTrace(String name) {
		return logTrace.contains(name);
	}

	@Override
	public Base base() {
		return getBaseControl().base();
	}

	@Override
	public void setLogFactory(LogFactory logFactory) {
		this.logFactory = logFactory;
	}

	@Override
	public Set<String> getLogTrace() {
		return logTrace;
	}

	@Override
	public void setBaseDir(File file) {
		baseDir  = file;
		baseDir.mkdirs();
	}

	@Override
	public File getFile(SCOPE scope,String dir) {
		dir = MFile.normalizePath(dir);
		if (scope == SCOPE.TMP)
			return new File(MSystem.getTmpDirectory() + File.pathSeparator + dir);
		if (scope == SCOPE.LOG) {
			File log = new File(baseDir,"logs");
			if (log.exists() && log.isDirectory())
				return new File(log, dir);
		}
		return new File(baseDir, dir);
	}

	@Override
	public String getSystemProperty(String name, String def) {
		return System.getProperty(name, def);
	}

	@Override
	public synchronized Log lookupLog(Object owner) {
		if (mlogFactory == null)
			mlogFactory = M.l(MLogFactory.class);
		return mlogFactory.lookup(owner);
	}

	@Override
	public void updateLog() {
		if (mlogFactory == null) return;
		mlogFactory.update();
	}
	
	@Override
	public void setMLogFactory(MLogFactory mlogFactory) {
		this.mlogFactory = mlogFactory;
	}

}
