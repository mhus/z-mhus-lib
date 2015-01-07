package de.mhus.lib.core.system;

import java.io.File;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.activator.ActivatorImpl;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.XmlConfigFile;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.service.ConfigProvider;

public class DefaultSingleton implements ISingleton, SingletonInitialize {

	private ConsoleFactory logFactory;
	private File baseDir;
	private IConfig config;
	private BaseControl baseControl;
	private ConfigProvider configProvider;
	

	@Override
	public Log createLog(Object owner) {
		String name = null;
		if (owner == null) {
			name = "?";
		} else
		if (owner instanceof Class) {
			name = ((Class<?>)owner).getName();
		} else
			name = String.valueOf(owner);
		return logFactory.getInstance(name);
	}

	@Override
	public void doInitialize(ClassLoader coreLoader) {
		logFactory = new ConsoleFactory();
		baseDir = new File(".");
	}

	public synchronized IConfig getConfig() {
		if (config == null) {
			File file = new File(baseDir,"config.xml");
			if (file.exists() && file.isFile())
				try {
					config = new XmlConfigFile(file);
				} catch (Exception e) {
				}
			if (config == null)
				config = new HashConfig();
		}
		return config;
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
		return new ActivatorImpl();
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
		ResourceNode logging = getConfig().getNode("trace");
		if (logging == null) return false;
		return logging.getBoolean(name, false);
	}

}
