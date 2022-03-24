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
package de.mhus.lib.core;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Date;

import de.mhus.lib.core.cfg.MCfgUpdater;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.mapi.ApiInitialize;
import de.mhus.lib.core.mapi.DefaultMApi;
import de.mhus.lib.core.mapi.DummyClass;
import de.mhus.lib.core.mapi.IApi;
import de.mhus.lib.core.mapi.IApiFactory;
import de.mhus.lib.core.node.INode;
import de.mhus.lib.errors.TimeoutRuntimeException;

public class MApi {

    public enum SCOPE {
        LOG,
        TMP,
        ETC,
        DEPLOY,
        DATA
    }

    private static IApi api;
    private static Log log = null;
    protected static Boolean trace;
    //	private static WeakHashMap<UUID, Log> loggers = new WeakHashMap<>();
    private static MCfgUpdater configUpdater;
    public static PrintStream out =
            System.out; // catch default system out while startup (gogo shell will change stdout)
    public static PrintStream err =
            System.err; // catch default system out while startup (gogo shell will change stdout)

    //	private static DummyClass dummy = new DummyClass(); // the class is inside this bundle and
    // has the correct class loader

    private MApi() {}

    public static synchronized IApi get() {
        if (api == null) {
            try {
                IApi obj = null;
                String path = "de.mhus.lib.mutable.MApiFactory";
                if (System.getProperty("mhu.lib.api.factory") != null)
                    path = System.getProperty(M.PROP_API_FACTORY_CLASS);
                dirtyLogDebug("MApiFactory", path);
                IApiFactory factory =
                        (IApiFactory) Class.forName(path).getDeclaredConstructor().newInstance();
                if (factory != null) {
                    obj = factory.createApi();
                }
                api = obj;
            } catch (Throwable t) {
                if (isDirtyTrace()) {
                    System.err.println(MDate.toIsoDateTime(new Date()) + " ERROR getting IApi");
                    t.printStackTrace();
                }
            }
            if (api == null) api = new DefaultMApi();
            if (isDirtyTrace())
                System.out.println(
                        MDate.toIsoDateTime(new Date())
                                + " MApi implementation: "
                                + api.getClass().getCanonicalName());
            if (api instanceof ApiInitialize)
                ((ApiInitialize) api).doInitialize(DummyClass.class.getClassLoader());
            // init local log at the end
            log = Log.getLog(MApi.class);
        }
        return api;
    }

    public static boolean isDirtyTrace() {
        if (trace == null) trace = "true".equals(System.getProperty(M.PROP_DIRTY_TRACE));
        return trace;
    }

    public static void setDirtyTrace(boolean dt) {
        trace = dt;
    }

    public static boolean isTrace(String name) {
        // dirtyLog("Ask for trace", name);
        //		String value = System.getProperty(name+".trace");
        //		if (value != null) return "true".equals(value);
        return get().isTrace(name);
    }

    //    public static void doStartTrailLog(String source) {
    //        LevelMapper mapper = get().getLogFactory().getLevelMapper();
    //        if (mapper != null && mapper instanceof TrailLevelMapper)
    //            ((TrailLevelMapper) mapper).doConfigureTrail(source, MLogUtil.MAP_LABEL);
    //    }
    //
    //    public static void doStopTrailLog() {
    //        LevelMapper mapper = get().getLogFactory().getLevelMapper();
    //        if (mapper != null && mapper instanceof TrailLevelMapper)
    //            ((TrailLevelMapper) mapper).doResetTrail();
    //    }

    //	public static void registerLogger(Log log) {
    //		synchronized (loggers) {
    //			loggers.put(log.getId(), log);
    //		}
    //	}
    //
    //	public static void unregisterLogger(Log log) {
    //		synchronized (loggers) {
    //			loggers.remove(log.getId());
    //		}
    //	}
    //
    public static void updateLoggers() {
        Log.getLog(MApi.class);
        get().updateLog();
        //		try {
        //			synchronized (loggers) {
        //				for (UUID logId : loggers.keySet().toArray(new UUID[loggers.size()]))
        //					loggers.get(logId).update();
        //			}
        //		} catch(Throwable t) {
        //			if (MApi.isDirtyTrace()) t.printStackTrace();
        //		}
    }

    public static INode getCfg(Object owner, INode def) {
        return get().getCfgManager().getCfg(owner, def);
    }

    /**
     * Returns the config or an empty config as default.
     *
     * @param owner
     * @return the config
     */
    public static INode getCfg(Object owner) {
        return get().getCfgManager().getCfg(owner);
    }

    public static synchronized MCfgUpdater getCfgUpdater() {
        if (configUpdater == null) configUpdater = new MCfgUpdater();
        return configUpdater;
    }

    public static File getFile(SCOPE scope, String path) {
        return get().getFile(scope, path);
    }

    public static <T> T lookup(Class<T> class1) {
        return get().lookup(class1);
    }

    public static <T, D extends T> T lookup(Class<T> class1, Class<D> def) {
        return get().lookup(class1, def);
    }

    public static <T extends Object> T waitFor(Class<? extends T> ifc, long timeout) {
        long start = System.currentTimeMillis();
        while (true) {
            try {
                T api = lookup(ifc);
                if (api != null) // should not happen
                return api;
            } catch (Throwable t) {
            }
            if (System.currentTimeMillis() - start > timeout)
                throw new TimeoutRuntimeException("timeout getting API", ifc);
            MThread.sleep(500);
        }
    }

    public static void dirtyLogTrace(Object... string) {
        if (log != null) {
            log.t("dirtyLog", string);
            return;
        }
        if (string == null || !isDirtyTrace()) return;
        err.println(MDate.toIsoDateTime(new Date()) + " TRACE " + Arrays.toString(string));
        for (Object s : string) if (s instanceof Throwable) ((Throwable) s).printStackTrace(err);
    }

    public static void dirtyLogDebug(Object... string) {
        if (log != null) {
            log.d("dirtyLog", string);
            return;
        }
        if (string == null || !isDirtyTrace()) return;
        err.println(MDate.toIsoDateTime(new Date()) + " DEBUG " + Arrays.toString(string));
        for (Object s : string) if (s instanceof Throwable) ((Throwable) s).printStackTrace(err);
    }

    public static void dirtyLogInfo(Object... string) {
        if (log != null) {
            log.i("dirtyLog", string);
            return;
        }
        if (!isDirtyTrace()) return;
        if (string == null) return;
        err.println(MDate.toIsoDateTime(new Date()) + " INFO " + Arrays.toString(string));
        for (Object s : string) if (s instanceof Throwable) ((Throwable) s).printStackTrace(err);
    }

    public static void dirtyLogError(Object... string) {
        if (log != null) {
            log.e("dirtyLog", string);
            return;
        }
        if (string == null) return;
        err.println(MDate.toIsoDateTime(new Date()) + " ERROR " + Arrays.toString(string));
        for (Object s : string) if (s instanceof Throwable) ((Throwable) s).printStackTrace(err);
    }

    public static String getSystemProperty(String name, String def) {
        String value = System.getProperty(name);
        if (value == null) {
            switch (name) {
                case M.PROP_CONFIG_FILE:
                    {
                        String file = M.DEFAULT_MHUS_CONFIG_FILE;
                        return getFile(MApi.SCOPE.ETC, file).getAbsolutePath();
                    }
                case M.PROP_TIMER_CONFIG_FILE:
                    {
                        String file = M.DEFAULT_MHUS_TIMER_CONFIG_FILE;
                        file = get().getCfgString(IApi.class, M.PROP_TIMER_CONFIG_FILE, file);
                        return getFile(MApi.SCOPE.ETC, M.DEFAULT_MHUS_TIMER_CONFIG_FILE)
                                .getAbsolutePath();
                    }
                default:
                    return def;
            }
        }
        return value;
    }
}
