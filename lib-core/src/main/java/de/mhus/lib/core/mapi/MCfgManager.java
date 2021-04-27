/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.mapi;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.annotations.activator.DefaultFactory;
import de.mhus.lib.core.M;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.cfg.CfgProvider;
import de.mhus.lib.core.node.DefaultNodeFactory;
import de.mhus.lib.core.node.INode;
import de.mhus.lib.core.node.INodeFactory;
import de.mhus.lib.core.node.MNode;

@DefaultFactory(DefaultMApiFactory.class)
public class MCfgManager {

    protected HashMap<String, CfgProvider> configurations = new HashMap<>();

    protected INodeFactory configFactory;

    protected LinkedList<File> mhusConfigFiles = new LinkedList<>();

    protected IApiInternal internal;

    public MCfgManager(IApiInternal internal) {
        this.internal = internal;
    }

    /**
     * Stop old provider Start new provider Update configurations
     *
     * @param provider
     */
    public void registerCfgProvider(CfgProvider provider) {
        if (provider == null) return;
        CfgProvider old = configurations.put(provider.getName(), provider);
        if (old != null) old.doStop();
        provider.doStart();
        MApi.getCfgUpdater().doUpdate(provider.getName());
    }

    /**
     * Stop old provider Update configuration
     *
     * @param name
     */
    public void unregisterCfgProvider(String name) {
        CfgProvider old = configurations.remove(name);
        if (old != null) old.doStop();
        MApi.getCfgUpdater().doUpdate(name);
    }

    @Override
    public String toString() {
        return configurations.keySet().toString();
    }

    public Collection<CfgProvider> getProviders() {
        return configurations.values();
    }

    /**
     * The getConfig without default value will return an empty configuration and not null if the
     * configuration is not found.
     *
     * @param owner
     * @return Always an configuration.
     */
    public INode getCfg(Object owner) {
        INode ret = getCfg(owner, null);
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
    public INode getCfg(Object owner, INode def) {

        Class<?> c = null;
        if (owner instanceof String) {
            String name = (String) owner;
            INode cClass = getCfg(name);
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
            INode cClass = getCfg(name);
            if (cClass != null) {
                //				log().t("found (2)",owner.getClass(),name);
                return cClass;
            }
            c = c.getSuperclass();
        }
        //		log().t("not found",owner.getClass());

        return def;
    }

    public INode getCfg(String owner) {

        CfgProvider p = configurations.get(owner);
        if (p != null) {
            INode cOwner = p.getConfig();
            if (cOwner != null) return cOwner;
        }
        return new MNode();
    }

    public INode getCfg(String owner, INode def) {

        INode cClass = getCfg(owner);
        if (cClass != null) {
            //			log().t("found (3)",owner.getClass(),owner);
            return cClass;
        }

        return def;
    }

    public void doRestart() {
        CfgProvider system = configurations.get(M.CFG_SYSTEM);
        if (system != null) {
            for (CfgProvider v :
                    new ArrayList<>(
                            configurations.values())) // java.util.ConcurrentModificationException
            v.doRestart();
        } else {
            initialConfiguration();
        }
        internal.updateSystemCfg(system);
    }

    protected void initialConfiguration() {
        CentralMhusCfgProvider provider = new CentralMhusCfgProvider();
        registerCfgProvider(provider);
    }

    public synchronized INodeFactory getConfigFactory() {
        if (configFactory == null) configFactory = new DefaultNodeFactory();
        return configFactory;
    }

    public class CentralMhusCfgProvider extends CfgProvider {

        private INode systemNode;
        private INode config;

        public CentralMhusCfgProvider() {
            super(M.CFG_SYSTEM);
        }

        @Override
        public synchronized INode getConfig() {
            return systemNode;
        }

        @Override
        public void doStart() {
            doRestart();
        }

        @Override
        public void doRestart() {

            LinkedList<File> fileList = new LinkedList<>();

            File configFile = new File(MApi.getSystemProperty(M.PROP_CONFIG_FILE, null));
            fileList.add(configFile);

            if (configFile.exists() && configFile.isFile())
                try {
                    MApi.dirtyLogInfo("Load config file", configFile);
                    config = getConfigFactory().read(configFile);
                    systemNode = config.getObject(M.CFG_SYSTEM);
                    if (systemNode != null) {
                        String includePattern = systemNode.getString("include", null);
                        if (includePattern != null) {

                            File i = new File(includePattern);
                            if (!i.isAbsolute())
                                i = new File(configFile.getParentFile(), includePattern);
                            for (File f : MFile.filter(i.getParentFile(), i.getName())) {

                                if (f.getName().endsWith(".xml") || f.getName().endsWith(".yaml")) {
                                    MApi.dirtyLogInfo("Load config file", f);
                                    INode cc = getConfigFactory().read(f);
                                    cc.setString("_source", f.getAbsolutePath());
                                    INode.merge(cc, config);
                                    fileList.add(f);
                                }
                            }
                        }
                    } else {
                        systemNode = new MNode();
                    }

                    for (INode owner : config.getObjects()) {
                        if (!owner.getName().equals(M.CFG_SYSTEM)) {
                            registerCfgProvider(new PartialConfigProvider(owner));
                        }
                    }
                    mhusConfigFiles = fileList;
                    return;
                } catch (Exception e) {
                    MApi.dirtyLogDebug(e);
                }

            MApi.dirtyLogDebug("*** MHUS Config file not found", configFile);
            config = new MNode(); // set empty config
            systemNode = new MNode();
        }

        @Override
        public void doStop() {
            configurations
                    .values()
                    .forEach(
                            v -> {
                                if (v instanceof PartialConfigProvider)
                                    unregisterCfgProvider(v.getName());
                            });
        }
    }

    private class PartialConfigProvider extends CfgProvider {

        private INode config;

        public PartialConfigProvider(INode config) {
            super(config.getName());
            this.config = config;
        }

        @Override
        public INode getConfig() {
            return config;
        }

        @Override
        public void doStart() {}

        @Override
        public void doStop() {}

        @Override
        public void doRestart() {}
    }

    public List<String> getOwners() {
        return new LinkedList<>(configurations.keySet());
    }

    /**
     * List of files for default mhus-config - inclusive includes. First entry is the mhus-config
     * file
     *
     * @return List of files
     */
    public List<File> getMhusConfigFiles() {
        return mhusConfigFiles;
    }

    public void reload(Object owner) {}

    /**
     * Return all entries of type key from the 'global' configuration section from inside the
     * providers configuration.
     *
     * <p>The method will ever return a list.
     *
     * @param provider The provider where to search in.
     * @param key Name of the section
     * @return A list of configurations
     */
    @SuppressWarnings("unchecked")
    public static List<INode> getGlobalConfigurations(CfgProvider provider, String key) {
        INode global = provider.getConfig().getObjectOrNull("global");
        if (global == null) return (List<INode>) MCollection.EMPTY_LIST;
        return global.getObjectList(key);
    }

    /**
     * Return all entries of type key from the 'global' configuration section from all providers.
     *
     * <p>The method will ever return a list.
     *
     * @param key Name of the section
     * @return A list of configurations
     */
    public static List<INode> getGlobalConfigurations(String key) {
        LinkedList<INode> out = new LinkedList<>();
        for (CfgProvider provider : new ArrayList<>(MApi.get().getCfgManager().getProviders())) {
            INode global = provider.getConfig().getObjectOrNull("global");
            if (global == null) continue;
            List<INode> list = global.getObjectList(key);
            out.addAll(list);
        }
        return out;
    }
}
