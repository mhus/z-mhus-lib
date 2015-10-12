package de.mhus.lib.core.system;

import java.util.HashSet;
import java.util.Set;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.activator.DefaultActivator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.configupdater.DefaultConfigLoader;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.service.ConfigProvider;

public class DefaultSingleton implements ISingleton, SingletonInitialize, ISingletonInternal {
	
	private LogFactory logFactory = new ConsoleFactory();
	private BaseControl baseControl;
	private ConfigProvider configProvider;
	private HashSet<String> logTrace = new HashSet<>();
	private DefaultConfigLoader cl = new DefaultConfigLoader();

	@Override
	public void doInitialize(ClassLoader coreLoader) {
		cl.doInitialize(this);
		reConfigure();
		
	}

	public synchronized IConfig getConfig() {
		return cl.getConfig();
	}
		
	@Override
	public void reConfigure() {
		cl.reConfigure();
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
	public synchronized ConfigProvider getConfigProvider() {
		if (configProvider == null) {
			configProvider = new ConfigProvider(getConfig());
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

}
