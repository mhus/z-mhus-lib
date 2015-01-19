package de.mhus.lib.core.system;

import java.io.File;
import java.util.HashSet;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MTimer;
import de.mhus.lib.core.activator.ActivatorImpl;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.XmlConfigFile;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.io.FileWatch;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.service.ConfigProvider;

public class DefaultSingleton implements ISingleton, SingletonInitialize {

	public static final String PROP_FILE_WATCH = "mhus.config.watch";
	public static final String PROP_CONFIG_FILE = "mhus.config.file";
	
	private ConsoleFactory logFactory;
	private File baseDir;
	private IConfig config;
	private BaseControl baseControl;
	private ConfigProvider configProvider;
	private boolean fullTrace;
	private HashSet<String> logTrace = new HashSet<>();
	private FileWatch fileWatch;
	private boolean needFileWatch = false;

	private String configFile;

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
		needFileWatch = "true".equals(System.getProperty(PROP_FILE_WATCH));
		configFile = System.getProperty(PROP_CONFIG_FILE, "mhus-config.xml");
		logFactory = new ConsoleFactory();
		baseDir = new File(".");
	}

	public synchronized IConfig getConfig() {
		if (config == null) {
			
			if (fileWatch != null) {
				fileWatch.doStop();
				fileWatch = null;
			}
			
			File f = new File(baseDir,configFile);
			if (fullTrace)
				System.out.println("Try to load mhus config from " + f.getAbsolutePath());
			internalLoadConfig(f);
			
			if (needFileWatch) {
				MTimer timer = baseControl.getCurrentBase().base(MTimer.class);
				fileWatch = new FileWatch(f, timer, new FileWatch.Listener() {
	
					@Override
					public void onFileChanged(FileWatch fileWatch) {
						File file = fileWatch.getFile();
						internalLoadConfig(file);
					}
	
					@Override
					public void onFileWatchError(FileWatch fileWatch, Throwable t) {
						if (fullTrace)
							t.printStackTrace();
					}
					
				}).doStart();
			}	
		}
		return config;
	}
	
	private void internalLoadConfig(File file) {
		if (file.exists() && file.isFile())
			try {
				config = new XmlConfigFile(file);
			} catch (Exception e) {
				if (fullTrace)
					e.printStackTrace();
			}
		if (config == null)
			config = new HashConfig();
		loadConfig();
	}
	
	private void loadConfig() {
		ResourceNode system = config.getNode("system");
		if (system == null) return;
		
		fullTrace = system.getBoolean("TRACE",false);
		logTrace.clear();
		for (String p : system.getPropertyKeys()) {
			if (p.startsWith("TRACE."))
				logTrace.add(p.substring(6));
		}
		try {
			String factoryClassName = system.getString("log.factory.class");
			if (MString.isSet(factoryClassName)) {
				logFactory = (ConsoleFactory) Class.forName(factoryClassName).newInstance();
			}
		} catch (Throwable t) {
			if (fullTrace) t.printStackTrace();
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
		return fullTrace || logTrace.contains(name);
	}

}
