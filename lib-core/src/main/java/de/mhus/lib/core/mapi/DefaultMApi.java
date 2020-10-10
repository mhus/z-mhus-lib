/**
 * Copyright (C) 2020 Mike Hummel (mh@mhus.de)
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.mhus.lib.core.M;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MApi.SCOPE;
import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MFile;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.activator.DefaultActivator;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.cfg.CfgProvider;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.ConsoleFactory;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.LogFactory;
import de.mhus.lib.core.logging.MLogFactory;
import de.mhus.lib.core.logging.PrintStreamFactory;

public class DefaultMApi implements IApi, ApiInitialize, IApiInternal {

    protected LogFactory logFactory = new PrintStreamFactory();
    //    private BaseControl baseControl;
    protected MCfgManager configProvider;
    protected HashSet<String> logTrace = new HashSet<>();
    protected File baseDir = new File(".");
    protected MLogFactory mlogFactory;
    protected DefaultActivator base = new DefaultActivator();
    private Map<SCOPE,File> fileScopeCache = new HashMap<>();

    @Override
    public void doInitialize(ClassLoader coreLoader) {
        logFactory = new ConsoleFactory();
        getCfgManager();
    }

    //    @Override
    //    public synchronized BaseControl getBaseControl() {
    //        if (baseControl == null) {
    //            baseControl = new BaseControl();
    //        }
    //        return baseControl;
    //    }

    @Override
    public MActivator createActivator() {
        return new DefaultActivator();
    }

    @Override
    public LogFactory getLogFactory() {
        return logFactory;
    }

    @Override
    public synchronized MCfgManager getCfgManager() {
        if (configProvider == null) {
            configProvider = createMCfgManager();
            configProvider.doRestart();
            startInitiators();
        }
        return configProvider;
    }

    protected MCfgManager createMCfgManager() {
        return new MCfgManager(this);
    }

    @Override
    public boolean isTrace(String name) {
        return logTrace.contains(name);
    }

    public void startInitiators() {

        MApi.dirtyLogInfo("Start mhu-lib initiators");

        TreeMap<String, Object[]> initiators = new TreeMap<>(); // execute in an ordered way
        // default
        initiators.put("001_system", new Object[] {new SystemCfgInitiator(), null});
        initiators.put("002_logger", new Object[] {new LogCfgInitiator(), null});

        // init initiators
        try {
            IConfig system = configProvider.getCfg(MConstants.CFG_SYSTEM);
            MApi.setDirtyTrace(system.getBoolean("log.trace", false));
            Log.setStacktraceTrace(system.getBoolean("stacktraceTrace", false));

            MActivator activator = MApi.get().createActivator();
            for (IConfig node : MCfgManager.getGlobalConfigurations("initiator")) {
                try {
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
                } catch (Throwable t) {
                    MApi.dirtyLogError("Can't load initiator", node, " Error: ", t);
                }
            }

            for (Object[] initiator : initiators.values())
                try {
                    CfgInitiator i = (CfgInitiator) initiator[0];
                    IConfig c = (IConfig) initiator[1];
                    MApi.dirtyLogInfo("run initiator", initiator[0].getClass());
                    i.doInitialize(this, configProvider, c);
                } catch (Throwable t) {
                    MApi.dirtyLogError("Can't initiate", initiator.getClass(), " Error: ", t);
                }

        } catch (Throwable t) {
            MApi.dirtyLogError("Can't initiate config ", t);
        }
        // MApi.getCfgUpdater().doUpdate(null);
    }

    //    @Override
    //    public MBase base() {
    //        return getBaseControl().base();
    //    }

    @Override
    public void setLogFactory(LogFactory logFactory) {
        this.logFactory = logFactory;
    }

    @Override
    public Set<String> getLogTrace() {
        return logTrace;
    }

    @Override
    public void setBaseDir(File file) {
        baseDir = file;
        baseDir.mkdirs();
    }

    @Override
    public File getFile(SCOPE scope, String dir) {
        dir = MFile.normalizePath(dir);
        File scopeDir = fileScopeCache.get(scope);
        if (scopeDir == null) {
            String dirStr = MSystem.getProperty(IApi.class, "directory_" + scope.name());
            if (dirStr == null) {
                if (scope == SCOPE.TMP)
                    dirStr = MSystem.getTmpDirectory();
                else
                if (scope == SCOPE.LOG) {
                    File log = new File(baseDir, "logs");
                    if (log.exists() && log.isDirectory()) dirStr = log.getAbsolutePath();
                }
                if (dirStr == null)
                    dirStr = baseDir.getAbsolutePath();
                scopeDir = new File(dirStr);
                fileScopeCache.put(scope, scopeDir);
            }
        }

        return new File (scopeDir, dir);
    }

    @Override
    public synchronized Log lookupLog(Object owner) {
        if (mlogFactory == null) mlogFactory = M.l(MLogFactory.class);
        return mlogFactory.lookup(owner);
    }

    @Override
    public void updateLog() {
        if (mlogFactory == null) return;
        mlogFactory.update();
    }

    @Override
    public void setMLogFactory(MLogFactory mlogFactory) {
        this.mlogFactory = mlogFactory;
    }

    @Override
    public <T, D extends T> T lookup(Class<T> ifc, Class<D> def) {
        return base.lookup(ifc, def);
    }

    @Override
    public DefaultActivator getLookupActivator() {
        return base;
    }

    @Override
    public void updateSystemCfg(CfgProvider system) {
        if (system == null) return;
    }
}
