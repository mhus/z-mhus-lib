package de.mhus.lib.mutable;

import java.io.File;
import java.util.Dictionary;
import java.util.HashSet;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.activator.DefaultActivator;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.XmlConfigFile;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
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

	private static final String CONFIG_FULL_TRACE = "log.full.trace";
	private static final String CONFIG_TRACE = "log.trace.";
	private static final String CONFIG_DIRTY_TRACE = "log.dirty.trace";
	private static final String CONFIG_FILE_NAME = "config.file.name";
	
	private JavaLoggerFactory logFactory;
	private File baseDir;
	private IConfig config;
	private BaseControl baseControl;
	private ConfigProvider configProvider;
	private boolean fullTrace = false;
	private HashSet<String> logTrace = new HashSet<>();

	private String configFileName = "mhus-config.xml";

	public IConfig getConfig() { //TODO load from service
		synchronized (this) {
			if (config == null) {
				String configFile = System.getProperty(MConstants.PROP_CONFIG_FILE, configFileName );
				File file = new File(baseDir,configFile);
				if (MSingleton.isDirtyTrace())
					System.out.println("--- Try to load mhus config from " + file.getAbsolutePath());
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
	}
	
	public void reloadConfig() {
		synchronized (this) {
			config = null;
			getConfig();
		}
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

	public boolean isFullTrace() {
		return fullTrace;
	}

	public void updateOsgiConfig(Dictionary<String, ?> config) {
		synchronized (this) {
			MProperties p = new MProperties(config);
			setFullTrace(p.getBoolean(CONFIG_FULL_TRACE, isFullTrace()));
			MSingleton.setDirtyTrace(p.getBoolean(CONFIG_DIRTY_TRACE, isFullTrace()));
			clearTrace();
			for (String name : p.keys()) {
				if (name.startsWith(CONFIG_TRACE) && p.getBoolean(name, false)) {
					setTrace(name.substring(CONFIG_TRACE.length()+1));
				}
			}
			configFileName = p.getString(CONFIG_FILE_NAME,configFileName);
			reloadConfig();
		}
	}
	
	@Override
	public Base base() {
		return getBaseControl().getCurrentBase();
	}

	@Override
	public void reConfigure() {
		// TODO Auto-generated method stub
		
	}

}
