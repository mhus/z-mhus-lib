package de.mhus.lib.mutable;

import java.io.File;
import java.util.HashSet;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.activator.ActivatorImpl;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.XmlConfigFile;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.service.ConfigProvider;
import de.mhus.lib.core.system.ISingleton;
import de.mhus.lib.core.system.SingletonInitialize;
import de.mhus.lib.logging.JavaLoggerFactory;

/**
 * TODO: Map config to service
 * TODO: Add MActivator with mapper to OSGi Services
 * 
 * @author mikehummel
 *
 */
public class KarafSingletonImpl implements ISingleton, SingletonInitialize {

	private JavaLoggerFactory logFactory;
	private File baseDir;
	private IConfig config;
	private BaseControl baseControl;
	private ConfigProvider configProvider;
	private boolean fullTrace = false;
	private HashSet<String> logTrace = new HashSet<>();

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

	public synchronized IConfig getConfig() { //TODO load from service
		if (config == null) {
			File file = new File(baseDir,"etc/mhus-config.xml");
			if (fullTrace)
				System.out.println("Try to load mhus config from " + file.getAbsolutePath());
			if (file.exists() && file.isFile())
				try {
					config = new XmlConfigFile(file);
				} catch (Exception e) {
					if (fullTrace)
						e.printStackTrace();
				}
			if (config == null)
				config = new HashConfig();
		}
		return config;
	}
	
	public void reloadConfig() {
		config = null;
		getConfig();
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
	public void doInitialize(ClassLoader coreLoader) {
		logFactory = new JavaLoggerFactory();
		baseDir = new File(".");

	}

	@Override
	public boolean isTrace(String name) {
		return fullTrace || logTrace.contains(name);
	}

	public void setFullTrace(boolean trace) {
		fullTrace = trace;
	}
	
	public void setTrace(String name) {
		logTrace.add(name);
	}
	
	public void clearTrace() {
		logTrace.clear();
	}

	public String[] getTraceNames() {
		return logTrace.toArray(new String[logTrace.size()]);
	}
	
}
