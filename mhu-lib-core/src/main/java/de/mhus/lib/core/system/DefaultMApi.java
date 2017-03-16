package de.mhus.lib.core.system;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.activator.DefaultActivator;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.LogFactory;

public class DefaultMApi implements IApi, ApiInitialize, IApiInternal {
	
	private LogFactory logFactory = new ConsoleFactory();
	private BaseControl baseControl;
	private CfgManager configProvider;
	private HashSet<String> logTrace = new HashSet<>();
	private File baseDir = new File(".");

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
		return getBaseControl().getCurrentBase();
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
	public File getFile(String dir) {
		return new File(baseDir, dir);
	}

	@Override
	public String getSystemProperty(String name, String def) {
		return System.getProperty(name, def);
	}
	
}
