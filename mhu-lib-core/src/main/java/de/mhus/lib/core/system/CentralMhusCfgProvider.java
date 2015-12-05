package de.mhus.lib.core.system;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.TreeMap;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.cfg.CfgProvider;
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
import de.mhus.lib.core.util.TimerIfc;

public class CentralMhusCfgProvider extends MLog implements CfgProvider {

	private IConfig config;
	private FileWatch fileWatch;
	private String configFile;
	private ISingletonInternal internal;
	private TreeMap<String,CfgInitiator> initiators = new TreeMap<>(); // execute in an ordered way
	{
		// default
		initiators.put("001_system", new SystemCfgInitiator());
		initiators.put("002_logger", new LogCfgInitiator());
	}

	public void doInitialize(ISingletonInternal internal) {
		this.internal = internal;
		
		configFile = System.getProperty(MConstants.PROP_PREFIX + MConstants.PROP_CONFIG_FILE);
		if (configFile == null)
			configFile = MConstants.DEFAULT_MHUS_CONFIG_FILE;
		
	}

	public void reConfigure() {
		
		log().i("Load mhu-lib configuration");
		
		// init initiators
		try {
			ResourceNode system = MSingleton.get().getCfgManager().getCfg("system");
			if (system != null) {
				MActivator activator = MSingleton.get().createActivator();
				for (ResourceNode node : system.getNodes()) {
					if ("initiator".equals(node.getName())) {
						String clazzName = node.getString("class");
						String name = node.getString("name", clazzName);
						String level = node.getString("level", "100");
						name = level + "_" + name;
						
						if ("none".equals(clazzName)) {
							log().t("remove initiator",name);
							initiators.remove(name);
						} else
						if (clazzName != null && !initiators.containsKey(name)) {
							log().t("add initiator",name);
							CfgInitiator initiator = activator.createObject(CfgInitiator.class, clazzName);
							initiators.put(name, initiator);
						}
					}
				}
			}
			
			for (CfgInitiator initiator : initiators.values())
				try {
					log().t("run initiator",initiator.getClass());
					initiator.doInitialize(internal, MSingleton.get().getCfgManager() );
				} catch (Throwable t) {
					log().t("Can't initiate",initiator.getClass(),t);
					if (MSingleton.isDirtyTrace()) {
						System.out.println("Can't initiate " + initiator.getClass() + " Error: " + t);
						t.printStackTrace();
					}
				}
			
		} catch (Throwable t) {
			log().t("Can't initiate config",t);
			if (MSingleton.isDirtyTrace()) {
				System.out.println("Can't initiate config " + t);
				t.printStackTrace();
			}
		}
		MSingleton.getCfgUpdater().doUpdate(null);
		
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
	public synchronized IConfig getConfig() {
		if (config == null) {
			
			config = new HashConfig();
			
			if (fileWatch != null) {
				fileWatch.doStop();
				fileWatch = null;
			}
			
			File f = new File(configFile);
			if (MSingleton.isDirtyTrace())
				System.out.println("--- Try to load mhus config from " + f.getAbsolutePath());
			internalLoadConfig(f);
			
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
		return config;
	}

	@Override
	public void doStart(String name) {
		
	}

	@Override
	public void doStop() {
		
	}
		
}
