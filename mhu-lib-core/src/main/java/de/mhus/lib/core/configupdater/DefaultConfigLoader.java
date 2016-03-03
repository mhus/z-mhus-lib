package de.mhus.lib.core.configupdater;

import java.io.File;
import java.io.PrintStream;

import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.XmlConfigFile;
import de.mhus.lib.core.directory.EmptyResourceNode;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.io.FileWatch;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.logging.MutableParameterMapper;
import de.mhus.lib.core.logging.ParameterEntryMapper;
import de.mhus.lib.core.logging.ParameterMapper;
import de.mhus.lib.core.system.ISingletonInternal;
import de.mhus.lib.core.system.SecureStreamToLogAdapter;
import de.mhus.lib.core.util.TimerIfc;

/**
 * <p>DefaultConfigLoader class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class DefaultConfigLoader extends MLog {

	private IConfig config;
	private FileWatch fileWatch;
	private boolean needFileWatch = false;
	private String configFile;
	private File baseDir;
	private ISingletonInternal internal;
	private static PrintStream stdOut = System.out;
	private static PrintStream stdErr = System.err;
	

	/**
	 * <p>doInitialize.</p>
	 *
	 * @param internal a {@link de.mhus.lib.core.system.ISingletonInternal} object.
	 */
	public void doInitialize(ISingletonInternal internal) {
		this.internal = internal;
		
		configFile = System.getProperty(MConstants.PROP_PREFIX + MConstants.PROP_CONFIG_FILE);
		if (configFile == null)
			configFile = MConstants.DEFAULT_MHUS_CONFIG_FILE;
		
		needFileWatch = true;
	}

	/**
	 * <p>reConfigure.</p>
	 */
	public void reConfigure() {
		
		log().i("Load mhu-lib configuration");
		ResourceNode system = getConfig().getNode("system");
		
		
		if (system == null) system = new EmptyResourceNode();
		
		internal.getLogTrace().clear();
		for (String p : system.getPropertyKeys()) {
			if (p.startsWith("TRACE."))
				internal.getLogTrace().add(p.substring(6));
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

		LogFactory logFactory = null;
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
		
		internal.setLogFactory(logFactory);
		MSingleton.updateLoggers();
		MSingleton.getConfigUpdater().doUpdate();
		
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

	/**
	 * <p>Getter for the field <code>config</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.config.IConfig} object.
	 */
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
				TimerIfc timer = MSingleton.get().getBaseControl().getCurrentBase().lookup(TimerIfc.class);
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
		
}
