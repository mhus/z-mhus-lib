/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.system;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.mhus.lib.annotations.activator.DefaultFactory;
import de.mhus.lib.core.M;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MHousekeeper;
import de.mhus.lib.core.MHousekeeperTask;
import de.mhus.lib.core.MPeriod;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.cfg.CfgProvider;
import de.mhus.lib.core.config.DefaultConfigFactory;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.IConfigFactory;
import de.mhus.lib.core.config.MConfig;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.SingleList;

@DefaultFactory(DefaultMApiFactory.class)
public class CfgManager {

    private HashMap<String, CfgProvider> configurations = new HashMap<>();
    private CentralMhusCfgProvider provider;
    private IApiInternal internal;

    private IConfig config;
    private File configFile;
    private TreeMap<String, Object[]> initiators = new TreeMap<>(); // execute in an ordered way
    private IConfigFactory configFactory;

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
        if (initiator == null) initiators.remove(name);
        else initiators.put(name, new Object[] {initiator, config});
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
        return configFile.getPath();
    }

    public List<CfgProvider> getProviders() {
        return new SingleList<CfgProvider>(provider); // currently only one provider
    }

    /**
     * The getConfig without default value will return an empty
     * configuration and not null if the configuration is not found.
     * 
     * @param owner
     * @return Always an configuration.
     */
    public IConfig getCfg(Object owner) {
        IConfig ret = getCfg(owner, null);
        if (ret == null) ret = getConfigFactory().create();
        return ret;
    }
    
    /**
     * Returns the found configuration or the default value.
     * 
     * @param owner
     * @param def
     * @return The configuration or def
     */
    public IConfig getCfg(Object owner, IConfig def) {
        initCfg();

        Class<?> c = null;
        if (owner instanceof String) {
            String name = (String) owner;
            IConfig cClass = getCfg(name);
            if (cClass != null) {
                //				log().t("found (1)",name);
                return cClass;
            }
        } else if (owner instanceof Class) {
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

    private void initCfg() {}

    public IConfig getCfg(String owner) {
        initCfg();

        CfgProvider p = configurations.get(owner);
        if (p != null) {
            IConfig cOwner = p.getConfig();
            if (cOwner != null) return cOwner;
        }
        IConfig defaultConfig = provider.getConfig();
        if (defaultConfig == null) return new MConfig();
        IConfig cOwner = defaultConfig.getObjectOrNull(owner);
        if (cOwner == null) cOwner = new MConfig();
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

    public synchronized IConfigFactory getConfigFactory() {
        if (configFactory == null)
            configFactory = new DefaultConfigFactory();
        return configFactory;
    }


    public class CentralMhusCfgProvider implements CfgProvider {

        private HashMap<File, Long> configFiles = new HashMap<>();
        private MHousekeeperTask fileWatch =
                new MHousekeeperTask("mhus-config-watch") {

                    @Override
                    protected void doit() throws Exception {
                        boolean ok = true;
                        synchronized (configFiles) {
                            for (Map.Entry<File, Long> entry : configFiles.entrySet()) {
                                if (entry.getKey().lastModified() != entry.getValue()) {
                                    ok = false;
                                    break;
                                }
                            }
                        }
                        if (!ok) {
                            if (internalLoadConfig()) reConfigure();
                        }
                    }
                };

        public void doInitialize() {
            configFile = new File(MApi.get().getSystemProperty(MConstants.PROP_CONFIG_FILE, null));
            MHousekeeper housekeeper = M.l(MHousekeeper.class);
            housekeeper.register(fileWatch, MPeriod.MINUTE_IN_MILLISECOUNDS);
        }

        public void reConfigure() {

            MApi.dirtyLogInfo("Load mhu-lib configuration");

            // init initiators
            try {
                IConfig system = MApi.get().getCfgManager().getCfg("system");
                if (system != null) {
                    MApi.setDirtyTrace(system.getBoolean("log.trace", false));
                    Log.setStacktraceTrace(system.getBoolean("stacktraceTrace", false));
                }
                for (String owner : MApi.get().getCfgManager().getOwners()) {
                    IConfig cfg = MApi.get().getCfgManager().getCfg(owner);
                    MActivator activator = MApi.get().createActivator();
                    for (IConfig node : cfg.getObjects()) {
                        try {
                            if ("initiator".equals(node.getName())) {
                                String clazzName = node.getString("class");
                                String name = node.getString("name", clazzName);
                                String level = node.getString("level", "100");
                                name = level + "_" + name;

                                if ("none".equals(clazzName)) {
                                    MApi.dirtyLogDebug("remove initiator", name);
                                    initiators.remove(name);
                                } else if (clazzName != null && !initiators.containsKey(name)) {
                                    MApi.dirtyLogDebug("add initiator", name);
                                    CfgInitiator initiator =
                                            activator.createObject(CfgInitiator.class, clazzName);
                                    initiators.put(name, new Object[] {initiator, node});
                                }
                            }
                        } catch (Throwable t) {
                            MApi.dirtyLogError("Can't load initiator", node, " Error: ", t);
                        }
                    }
                }

                for (Object[] initiator : initiators.values())
                    try {
                        CfgInitiator i = (CfgInitiator) initiator[0];
                        IConfig c = (IConfig) initiator[1];
                        MApi.dirtyLogInfo("run initiator", initiator[0].getClass());
                        i.doInitialize(internal, MApi.get().getCfgManager(), c);
                    } catch (Throwable t) {
                        MApi.dirtyLogError("Can't initiate", initiator.getClass(), " Error: ", t);
                    }

            } catch (Throwable t) {
                MApi.dirtyLogError("Can't initiate config ", t);
            }
            MApi.getCfgUpdater().doUpdate(null);
        }

        public synchronized boolean internalLoadConfig() {
            if (configFile.exists() && configFile.isFile())
                try {
                    synchronized (configFiles) {
                        MApi.dirtyLogInfo("Load config file", configFile);
                        IConfig c = getConfigFactory().read(configFile);
                        configFiles.clear();
                        configFiles.put(configFile, configFile.lastModified());
                        IConfig systemNode = c.getObject("system");
                        if (systemNode != null) {
                            String includePattern = systemNode.getString("include", null);
                            if (includePattern != null) {

                                File i = new File(includePattern);
                                if (!i.isAbsolute())
                                    i = new File(configFile.getParentFile(), includePattern);
                                for (File f : MFile.filter(i.getParentFile(), i.getName())) {
                                    if (f.getName().endsWith(".xml")) {
                                        MApi.dirtyLogInfo("Load config file", f);
                                        IConfig cc = getConfigFactory().read(f);
                                        configFiles.put(f, f.lastModified());
                                        cc.setString("_source", f.getAbsolutePath());
                                        MConfig.merge(cc, c);
                                    }
                                }
                            }
                        }
                        config = c;
                        lastConfigUpdate = System.currentTimeMillis();
                    }
                    return true;
                } catch (Exception e) {
                    MApi.dirtyLogDebug(e);
                }

            MApi.dirtyLogDebug("*** MHUS Config file not found", configFile);

            return false;
        }

        @Override
        public synchronized IConfig getConfig() {
            if (config == null) {
                config = getConfigFactory().create();
                internalLoadConfig();
            }
            return config;
        }

        @Override
        public void doStart(String name) {}

        @Override
        public void doStop() {}

        public Set<File> files() {
            return configFiles.keySet();
        }
    }

    public List<String> getOwners() {
        initCfg();
        return new LinkedList<>(configurations.keySet());
    }

    public long getLastConfigUpdate() {
        return lastConfigUpdate;
    }
}
