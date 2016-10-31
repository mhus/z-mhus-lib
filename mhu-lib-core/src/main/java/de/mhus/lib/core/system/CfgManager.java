package de.mhus.lib.core.system;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

import de.mhus.lib.annotations.activator.DefaultFactory;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.cfg.CfgProvider;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.XmlConfigFile;
import de.mhus.lib.core.io.FileWatch;
import de.mhus.lib.core.util.TimerIfc;

@DefaultFactory(DefaultSingletonFactory.class)
public class CfgManager {

	private HashMap<String, CfgProvider> configurations = new HashMap<>();
	private CentralMhusCfgProvider provider;
	private ISingletonInternal internal;
	
	private IConfig config;
	private FileWatch fileWatch;
	private String configFile;
	private TreeMap<String,CfgInitiator> initiators = new TreeMap<>(); // execute in an ordered way
	{
		// default
		initiators.put("001_system", new SystemCfgInitiator());
		initiators.put("002_logger", new LogCfgInitiator());
	}

	
	public CfgManager(ISingletonInternal internal) {
		this.internal = internal;
		provider = new CentralMhusCfgProvider();
		provider.doInitialize();
	}
	
	public void registerCfgInitiator(String name, CfgInitiator initiator) {
		if (initiator == null)
			initiators.remove(name);
		else
			initiators.put(name, initiator);
	}
	
	public void registerCfgProvider(String name, CfgProvider provider) {
		if (name == null) return;
		if (provider == null) {
			CfgProvider old = configurations.remove(name);
			if (old != null) old.doStop();
		} else {
			configurations.put(name, provider);
			provider.doStart(name);
		}
	}
	
	public IConfig getCfg(Object owner, IConfig def) {
		initCfg();
		
		Class<?> c = null;
		if (owner instanceof String) {
			String name = (String)owner;
			IConfig cClass = getCfg(name);
			if (cClass != null) {
//				log().t("found (1)",name);
				return cClass;
			}
		} else
		if (owner instanceof Class) {
			c = (Class<?>) owner;
		} else {
			c = owner.getClass();
		}
		while (c != null) {
			String name = c.getCanonicalName();
			IConfig cClass = getCfg(name);
			if (cClass != null) {
//				log().t("found (2)",owner.getClass(),name);
				return cClass;
			}
			c = c.getSuperclass();
		}
//		log().t("not found",owner.getClass());			
	
		return def;
	}
	
	private void initCfg() {
		
	}

	public IConfig getCfg(String owner) {
		initCfg();
		
		CfgProvider p = configurations.get(owner);
		if (p != null) {
			IConfig cOwner = p.getConfig();
			if (cOwner != null) return cOwner;
		}
		IConfig defaultConfig = provider.getConfig();
		if (defaultConfig == null) return null;
		IConfig  cOwner = defaultConfig.getNode(owner);
		return cOwner;
	}
	
	public IConfig getCfg(String owner, IConfig def) {
		initCfg();

		IConfig cClass = getCfg(owner);
		if (cClass != null) {
//			log().t("found (3)",owner.getClass(),owner);
			return cClass;
		}

		return def;
	}

	public boolean isOwner(Object owner, String n) {

		Class<?> c = null;
		if (owner instanceof String) {
			String name = (String)owner;
			IConfig cClass = getCfg(name);
			if (cClass != null) {
//				log().t("found (1)",name);
				return name.equals(n);
			}
		} else
		if (owner instanceof Class) {
			c = (Class<?>) owner;
		} else {
			c = owner.getClass();
		}
		while (c != null) {
			String name = c.getCanonicalName();
			IConfig cClass = getCfg(name);
			if (cClass != null) {
//				log().t("found (2)",owner.getClass(),name);
				return name.equals(n);
			}
			c = c.getSuperclass();
		}
		
		return false;
	}

	public void reConfigure() {
		provider.reConfigure();
	}

	class CentralMhusCfgProvider implements CfgProvider {

		public void doInitialize() {
			configFile = MSingleton.get().getSystemProperty(MConstants.PROP_CONFIG_FILE, MConstants.DEFAULT_MHUS_CONFIG_FILE);
		}

		public void reConfigure() {
			
			MSingleton.dirtyLog("Load mhu-lib configuration");
			
			// init initiators
			try {
				IConfig system = MSingleton.get().getCfgManager().getCfg("system");
				if (system != null) {
					MActivator activator = MSingleton.get().createActivator();
					for (IConfig node : system.getNodes()) {
						if ("initiator".equals(node.getName())) {
							String clazzName = node.getString("class");
							String name = node.getString("name", clazzName);
							String level = node.getString("level", "100");
							name = level + "_" + name;
							
							if ("none".equals(clazzName)) {
								MSingleton.dirtyLog("remove initiator",name);
								initiators.remove(name);
							} else
							if (clazzName != null && !initiators.containsKey(name)) {
								MSingleton.dirtyLog("add initiator",name);
								CfgInitiator initiator = activator.createObject(CfgInitiator.class, clazzName);
								initiators.put(name, initiator);
							}
						}
					}
				}
				
				for (CfgInitiator initiator : initiators.values())
					try {
						MSingleton.dirtyLog("run initiator",initiator.getClass());
						initiator.doInitialize(internal, MSingleton.get().getCfgManager() );
					} catch (Throwable t) {
						MSingleton.dirtyLog("Can't initiate",initiator.getClass(),t);
						if (MSingleton.isDirtyTrace()) {
							System.out.println("Can't initiate " + initiator.getClass() + " Error: " + t);
							t.printStackTrace();
						}
					}
				
			} catch (Throwable t) {
				MSingleton.dirtyLog("Can't initiate config",t);
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
	
}
