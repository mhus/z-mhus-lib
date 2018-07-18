/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.system;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import de.mhus.lib.annotations.activator.DefaultFactory;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.base.service.TimerIfc;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.cfg.CfgProvider;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.XmlConfigFile;
import de.mhus.lib.core.io.FileWatch;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.SingleList;

@DefaultFactory(DefaultMApiFactory.class)
public class CfgManager {

	private HashMap<String, CfgProvider> configurations = new HashMap<>();
	private CentralMhusCfgProvider provider;
	private IApiInternal internal;
	
	private IConfig config;
	private FileWatch fileWatch;
	private String configFile;
	private TreeMap<String,Object[]> initiators = new TreeMap<>(); // execute in an ordered way
	private long lastConfigUpdate;
	{
		// default
		initiators.put("001_system", new Object[] {new SystemCfgInitiator(), null});
		initiators.put("002_logger", new Object[] {new LogCfgInitiator(), null});
	}

	
	public CfgManager(IApiInternal internal) {
		this.internal = internal;
		provider = new CentralMhusCfgProvider();
		provider.doInitialize();
	}
	
	public void registerCfgInitiator(String name, CfgInitiator initiator, IConfig config) {
		if (initiator == null)
			initiators.remove(name);
		else
			initiators.put(name, new Object[] {initiator, config });
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
	
	@Override
	public String toString() {
		return configFile;
	}
	
	public List<CfgProvider> getProviders() {
		return new SingleList<CfgProvider>(provider); // currently only one provider
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
/*
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
*/
	public void reConfigure() {
		provider.reConfigure();
	}

	class CentralMhusCfgProvider implements CfgProvider {

		public void doInitialize() {
			configFile = MApi.get().getSystemProperty(MConstants.PROP_CONFIG_FILE, MConstants.DEFAULT_MHUS_CONFIG_FILE);
		}

		public void reConfigure() {
			
			MApi.dirtyLog("Load mhu-lib configuration");
			
			// init initiators
			try {
				IConfig system = MApi.get().getCfgManager().getCfg("system");
				if (system != null) {
					MApi.setDirtyTrace(system.getBoolean("log.trace", false));
					Log.setStacktraceTrace(system.getBoolean("stacktraceTrace", false));
					MActivator activator = MApi.get().createActivator();
					for (IConfig node : system.getNodes()) {
						if ("initiator".equals(node.getName())) {
							String clazzName = node.getString("class");
							String name = node.getString("name", clazzName);
							String level = node.getString("level", "100");
							name = level + "_" + name;
							
							if ("none".equals(clazzName)) {
								MApi.dirtyLog("remove initiator",name);
								initiators.remove(name);
							} else
							if (clazzName != null && !initiators.containsKey(name)) {
								MApi.dirtyLog("add initiator",name);
								CfgInitiator initiator = activator.createObject(CfgInitiator.class, clazzName);
								initiators.put(name, new Object[] {initiator, node });
							}
						}
					}
				}
				
				for (Object[] initiator : initiators.values())
					try {
						CfgInitiator i = (CfgInitiator)initiator[0];
						IConfig c = (IConfig)initiator[1];
						MApi.dirtyLog("run initiator",initiator[0].getClass());
						i.doInitialize(internal, MApi.get().getCfgManager(), c );
					} catch (Throwable t) {
						MApi.dirtyLog("Can't initiate",initiator.getClass()," Error: ",t);
					}
				
			} catch (Throwable t) {
				MApi.dirtyLog("Can't initiate config ", t);
			}
			MApi.getCfgUpdater().doUpdate(null);
			
		}

		private boolean internalLoadConfig(File file) {
			if (file.exists() && file.isFile())
				try {
					XmlConfigFile c = new XmlConfigFile(file);
					config = c;
					lastConfigUpdate = System.currentTimeMillis();
					return true;
				} catch (Exception e) {
					MApi.dirtyLog(e);
				}
			
			MApi.dirtyLog("*** MHUS Config file not found", file);
			
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
				MApi.dirtyLog("--- Try to load mhus config from ", f.getAbsolutePath());
				internalLoadConfig(f);
				
				TimerIfc timer = MApi.get().getBaseControl().getCurrentBase().lookup(TimerIfc.class);
				fileWatch = new FileWatch(f, timer, new FileWatch.Listener() {

					@Override
					public void onFileChanged(FileWatch fileWatch) {
						File file = fileWatch.getFile();
						if (internalLoadConfig(file))
							reConfigure();
					}

					@Override
					public void onFileWatchError(FileWatch fileWatch, Throwable t) {
						MApi.dirtyLog(t);
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

	public List<String> getOwners() {
		initCfg();
		return new LinkedList<>( configurations.keySet() );
	}

	public long getLastConfigUpdate() {
		return lastConfigUpdate;
	}

}
