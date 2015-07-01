package de.mhus.lib.core.system;

import java.io.File;
import java.io.PrintStream;
import java.util.HashSet;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.MTimer;
import de.mhus.lib.core.activator.ActivatorImpl;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.XmlConfigFile;
import de.mhus.lib.core.directory.EmptyResourceNode;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.io.FileWatch;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.logging.MutableParameterMapper;
import de.mhus.lib.core.logging.ParameterEntryMapper;
import de.mhus.lib.core.logging.ParameterMapper;
import de.mhus.lib.core.logging.StreamToLogAdapter;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.service.ConfigProvider;
import de.mhus.lib.core.util.TimerIfc;

public class DefaultSingleton implements ISingleton, SingletonInitialize {
	
	private static PrintStream stdOut = System.out;
	private static PrintStream stdErr = System.err;
	
	private LogFactory logFactory = new ConsoleFactory();
	private File baseDir;
	private IConfig config;
	private BaseControl baseControl;
	private ConfigProvider configProvider;
	private HashSet<String> logTrace = new HashSet<>();
	private FileWatch fileWatch;
	private boolean needFileWatch = false;

	private String configFile;

	@Override
	public void doInitialize(ClassLoader coreLoader) {
		configFile = System.getProperty(MConstants.PROP_PREFIX + MConstants.PROP_CONFIG_FILE);
		if (configFile == null)
			configFile = MConstants.DEFAULT_MHUS_CONFIG_FILE;
		else
			needFileWatch = true;
		
		reConfigure();
		
	}

	public synchronized IConfig getConfig() {
		if (config == null) {
			
			config = new HashConfig();
			
			if (fileWatch != null) {
				fileWatch.doStop();
				fileWatch = null;
			}
			
			File f = new File(baseDir,configFile);
			if (MSingleton.isDirtyTrace())
				System.out.println("--- Try to load mhus config from " + f.getAbsolutePath());
			if (!internalLoadConfig(f))
				return config;
			
			if (needFileWatch) {
				TimerIfc timer = baseControl.getCurrentBase().base(TimerIfc.class);
				fileWatch = new FileWatch(f, timer, new FileWatch.Listener() {
	
					@Override
					public void onFileChanged(FileWatch fileWatch) {
						File file = fileWatch.getFile();
						if (internalLoadConfig(file))
							reConfigure();
					}
	
					@Override
					public void onFileWatchError(FileWatch fileWatch, Throwable t) {
						if (MSingleton.isDirtyTrace())
							t.printStackTrace();
					}
					
				}).doStart();
			}	
		}
		return config;
	}
	
	private boolean internalLoadConfig(File file) {
		if (file.exists() && file.isFile())
			try {
				XmlConfigFile c = new XmlConfigFile(file);
				config = c;
				return true;
			} catch (Exception e) {
				if (MSingleton.isDirtyTrace())
					e.printStackTrace();
			}
		
		if (MSingleton.isDirtyTrace())
			System.out.println("*** MHUS Config file not found" + file);
		
		return false;
	}
	
	@Override
	public void reConfigure() {
		ResourceNode system = getConfig().getNode("system");
		
		
		if (system == null) system = new EmptyResourceNode();
		
		logTrace.clear();
		for (String p : system.getPropertyKeys()) {
			if (p.startsWith("TRACE."))
				logTrace.add(p.substring(6));
		}

		try {
			String key = MConstants.PROP_BASE_DIR;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			String baseDirName = ".";
			if (MString.isSet(name)) 
				baseDirName = name;
			baseDir = new File(baseDirName);
		} catch (Throwable t) {if (MSingleton.isDirtyTrace()) t.printStackTrace();}	

		try {
			String key = MConstants.PROP_LOG_FACTORY_CLASS;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			if (MString.isSet(name)) {
				logFactory = (LogFactory) Class.forName(name.trim()).newInstance();
			}
		} catch (Throwable t) {if (MSingleton.isDirtyTrace()) t.printStackTrace();}	
		if (logFactory == null)
			logFactory = new ConsoleFactory();

		try {
			String key = MConstants.PROP_LOG_LEVEL_MAPPER_CLASS;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			if (MString.isSet(name)) {
				logFactory.setLevelMapper( (LevelMapper) Class.forName(name.trim()).newInstance() );
			}
		} catch (Throwable t) {if (MSingleton.isDirtyTrace()) t.printStackTrace();}
		try {
			String key = MConstants.PROP_LOG_PARAMETER_MAPPER_CLASS;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			if (MString.isSet(name)) {
				logFactory.setParameterMapper( (ParameterMapper) Class.forName(name.trim()).newInstance() );
			}
		} catch (Throwable t) {if (MSingleton.isDirtyTrace()) t.printStackTrace();}
		
		if (logFactory.getParameterMapper() != null && logFactory.getParameterMapper() instanceof MutableParameterMapper) {
			try {
				ResourceNode[] mappers = system.getNodes(MConstants.PROP_LOG_PARAMETER_MAPPER_CLASS);
				if (mappers.length > 0) ((MutableParameterMapper)logFactory.getParameterMapper()).clear();
				for (ResourceNode mapper : mappers) {
					String name = mapper.getString("name");
					String clazz = mapper.getString("class");
					if (MString.isSet(name) && MString.isSet(clazz))
						((MutableParameterMapper)logFactory.getParameterMapper()).put(name, (ParameterEntryMapper) Class.forName(clazz.trim()).newInstance() );
				}
			} catch (Throwable t) {if (MSingleton.isDirtyTrace()) t.printStackTrace();}
		}
			
		try {
			String key = MConstants.PROP_LOG_CONSOLE_REDIRECT;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			if (MString.isSet(name)) {
				if ("true".equals(name)) {
					System.setErr(new SecureStreamToLogAdapter(LEVEL.ERROR, stdErr));
					System.setOut(new SecureStreamToLogAdapter(LEVEL.INFO, stdOut));
				}
			}
		} catch (Throwable t) {if (MSingleton.isDirtyTrace()) t.printStackTrace();}
		
		MSingleton.updateLoggers();
		
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
		return logTrace.contains(name);
	}

	@Override
	public Base base() {
		return getBaseControl().getCurrentBase();
	}

}
