package de.mhus.lib.core.system;

import java.io.PrintStream;
import java.util.Collection;

import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.PropertiesConfig;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log.LEVEL;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.logging.MLogFactory;
import de.mhus.lib.core.logging.MutableParameterMapper;
import de.mhus.lib.core.logging.ParameterEntryMapper;
import de.mhus.lib.core.logging.ParameterMapper;

public class LogCfgInitiator implements CfgInitiator {

	private static PrintStream err;
	private static PrintStream out;
	static {
		err = System.err;
		out = System.out;
	}
	
	@Override
	public void doInitialize(IApiInternal internal, CfgManager manager) {

		IConfig system = manager.getCfg("system");
		
		
		if (system == null) system = new PropertiesConfig(); // empty
		
		internal.getLogTrace().clear();
		for (String p : system.getPropertyKeys()) {
			if (p.startsWith("TRACE."))
				internal.getLogTrace().add(p.substring(6));
		}

		MLogFactory mlogFactory = null;
		try {
			String key = MConstants.PROP_LOG_MLOG_FACTORY_CLASS;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			if (MString.isSet(name)) {
				mlogFactory = (MLogFactory) Class.forName(name.trim()).newInstance();
			}
		} catch (Throwable t) {MApi.dirtyLog(t);}	
		if (mlogFactory != null)
			internal.setMLogFactory(mlogFactory);
		
		LogFactory logFactory = null;
		try {
			String key = MConstants.PROP_LOG_FACTORY_CLASS;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			if (MString.isSet(name)) {
				logFactory = (LogFactory) Class.forName(name.trim()).newInstance();
			}
		} catch (Throwable t) {MApi.dirtyLog(t);}	
		if (logFactory == null)
			logFactory = new ConsoleFactory();

		try {
			String key = MConstants.PROP_LOG_LEVEL_MAPPER_CLASS;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			if (MString.isSet(name)) {
				logFactory.setLevelMapper( (LevelMapper) Class.forName(name.trim()).newInstance() );
			}
		} catch (Throwable t) {MApi.dirtyLog(t);}
		
		try {
			String key = MConstants.PROP_LOG_MAX_MESSAGE_SIZE;
			String size = system.getString(key);
			if (size != null) {
				logFactory.setMaxMessageSize(Integer.valueOf(size));
			}
		} catch (Throwable t) {MApi.dirtyLog(t);}

		try {
			String key = MConstants.PROP_LOG_PARAMETER_MAPPER_CLASS;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			if (MString.isSet(name)) {
				logFactory.setParameterMapper( (ParameterMapper) Class.forName(name.trim()).newInstance() );
			}
		} catch (Throwable t) {MApi.dirtyLog(t);}
		
		if (logFactory.getParameterMapper() != null && logFactory.getParameterMapper() instanceof MutableParameterMapper) {
			try {
				Collection<IConfig> mappers = system.getNodes(MConstants.PROP_LOG_PARAMETER_MAPPER_CLASS);
				if (mappers.size() > 0) ((MutableParameterMapper)logFactory.getParameterMapper()).clear();
				for (IConfig mapper : mappers) {
					String name = mapper.getString("name");
					String clazz = mapper.getString("class");
					if (MString.isSet(name) && MString.isSet(clazz))
						((MutableParameterMapper)logFactory.getParameterMapper()).put(name, (ParameterEntryMapper) Class.forName(clazz.trim()).newInstance() );
				}
			} catch (Throwable t) {MApi.dirtyLog(t);}
		}
			
		try {
			String key = MConstants.PROP_LOG_CONSOLE_REDIRECT;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			if (MString.isSet(name)) {
				if ("true".equals(name)) {
					System.setErr(new SecureStreamToLogAdapter(LEVEL.ERROR, err));
					System.setOut(new SecureStreamToLogAdapter(LEVEL.INFO, out));
				}
			}
		} catch (Throwable t) {MApi.dirtyLog(t);}
		
		internal.setLogFactory(logFactory);

		
		MApi.updateLoggers();
		
	}

}
