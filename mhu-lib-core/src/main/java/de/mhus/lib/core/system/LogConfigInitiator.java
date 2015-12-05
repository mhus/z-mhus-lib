package de.mhus.lib.core.system;

import java.io.File;
import java.io.PrintStream;

import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.EmptyResourceNode;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.logging.MutableParameterMapper;
import de.mhus.lib.core.logging.ParameterEntryMapper;
import de.mhus.lib.core.logging.ParameterMapper;
import de.mhus.lib.core.logging.Log.LEVEL;

public class LogConfigInitiator implements ConfigInitiator {

	@Override
	public void doInitialize(ISingletonInternal internal, ConfigManager manager) {

		ResourceNode system = manager.getConfig("system");
		
		
		if (system == null) system = new EmptyResourceNode();
		
		internal.getLogTrace().clear();
		for (String p : system.getPropertyKeys()) {
			if (p.startsWith("TRACE."))
				internal.getLogTrace().add(p.substring(6));
		}

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
					System.setErr(null);
					System.setOut(null);
					System.setErr(new SecureStreamToLogAdapter(LEVEL.ERROR, System.err));
					System.setOut(new SecureStreamToLogAdapter(LEVEL.INFO, System.out));
				}
			}
		} catch (Throwable t) {if (MSingleton.isDirtyTrace()) t.printStackTrace();}
		
		internal.setLogFactory(logFactory);

		
		MSingleton.updateLoggers();
		
	}

}
