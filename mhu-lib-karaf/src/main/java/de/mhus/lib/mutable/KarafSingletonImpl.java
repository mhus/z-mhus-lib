package de.mhus.lib.mutable;

import java.io.File;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MHousekeeper;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.activator.DefaultActivator;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.XmlConfigFile;
import de.mhus.lib.core.configupdater.DefaultConfigLoader;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.service.ConfigProvider;
import de.mhus.lib.core.system.ISingleton;
import de.mhus.lib.core.system.ISingletonInternal;
import de.mhus.lib.core.system.SingletonInitialize;
import de.mhus.lib.core.util.TimerFactory;
import de.mhus.lib.core.util.TimerIfc;
import de.mhus.lib.karaf.MOsgi;
import de.mhus.lib.logging.JavaLoggerFactory;
import de.mhus.lib.logging.level.ThreadBasedMapper;

/**
 * TODO: Map config to service
 * TODO: Add MActivator with mapper to OSGi Services
 * 
 * @author mikehummel
 *
 */
public class KarafSingletonImpl implements ISingleton, SingletonInitialize, ISingletonInternal {
	
	private LogFactory logFactory;
	private BaseControl baseControl;
	private ConfigProvider configProvider;
	private boolean fullTrace = false;
	private HashSet<String> logTrace = new HashSet<>();

	private KarafHousekeeper housekeeper;
	
	private DefaultConfigLoader cl = new DefaultConfigLoader();

	public IConfig getConfig() {
		return cl.getConfig();
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
		cl.doInitialize(this);
		
		try {
			housekeeper = new KarafHousekeeper();
			getBaseControl().getCurrentBase().addObject(MHousekeeper.class, housekeeper);
		} catch (Throwable t) {
			System.out.println("Can't initialize housekeeper base: " + t);
		}
		try {
			TimerFactory timerFactory = MOsgi.getService(TimerFactory.class);
			TimerIfc timerIfc = timerFactory.getTimer();
			getBaseControl().getCurrentBase().addObject(TimerIfc.class, timerIfc);
		} catch (Throwable t) {
			System.out.println("Can't initialize timer base: " + t);
		}
		reConfigure();

		//logFactory.setLevelMapper(new ThreadBasedMapper() );
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

//	public void updateOsgiConfig(Dictionary<String, ?> config) {
//		synchronized (this) {
//			MProperties p = new MProperties(config);
//			setFullTrace(p.getBoolean(CONFIG_FULL_TRACE, isFullTrace()));
//			MSingleton.setDirtyTrace(p.getBoolean(CONFIG_DIRTY_TRACE, isFullTrace()));
//			clearTrace();
//			for (String name : p.keys()) {
//				if (name.startsWith(CONFIG_TRACE) && p.getBoolean(name, false)) {
//					setTrace(name.substring(CONFIG_TRACE.length()+1));
//				}
//			}
//			configFileName = p.getString(CONFIG_FILE_NAME,configFileName);
//			reloadConfig();
//		}
//	}

	@Override
	public Base base() {
		return getBaseControl().getCurrentBase();
	}

	@Override
	public void reConfigure() {
		cl.reConfigure();
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
