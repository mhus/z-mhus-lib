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
package de.mhus.lib.core;

import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;

import de.mhus.lib.core.cfg.UpdaterCfg;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.logging.TrailLevelMapper;
import de.mhus.lib.core.system.ApiInitialize;
import de.mhus.lib.core.system.DefaultMApi;
import de.mhus.lib.core.system.DummyClass;
import de.mhus.lib.core.system.IApi;
import de.mhus.lib.core.system.IApiFactory;
import de.mhus.lib.errors.TimeoutRuntimeException;

public class MApi {

    public enum SCOPE {LOG,TMP,ETC,DEPLOY,DATA}

	private static IApi api;
	protected static Boolean trace;
//	private static WeakHashMap<UUID, Log> loggers = new WeakHashMap<>();
	private static IConfig emptyConfig = null;
	private static UpdaterCfg configUpdater;
	public static PrintStream out = System.out; // catch default system out while startup (gogo shell will change stdout)
    public static PrintStream err = System.err; // catch default system out while startup (gogo shell will change stdout)
	
//	private static DummyClass dummy = new DummyClass(); // the class is inside this bundle and has the correct class loader
	
	private MApi() {}
	
	public static synchronized IApi get() {
		if (api == null) {
			try {
				IApi obj = null;
				String path = "de.mhus.lib.mutable.MApiFactory";
				if (System.getProperty("mhu.lib.api.factory") != null) path = System.getProperty(MConstants.PROP_API_FACTORY_CLASS);
				dirtyLog("MApiFactory", path);
				IApiFactory factory = (IApiFactory)Class.forName(path).getDeclaredConstructor().newInstance();
				if (factory != null) {
					obj = factory.createApi();
				}
				api = obj;
			} catch (Throwable t) {
				if (isDirtyTrace()) t.printStackTrace();
			}
			if (api == null)
				api = new DefaultMApi();
			if (isDirtyTrace()) System.out.println("--- MApi: " + api.getClass().getCanonicalName());
			if (api instanceof ApiInitialize)
				((ApiInitialize)api).doInitialize(DummyClass.class.getClassLoader());
		}
		return api;
	}
	
	
	public static boolean isDirtyTrace() {
		if (trace == null) trace = "true".equals(System.getProperty(MConstants.PROP_DIRTY_TRACE));
		return trace;
	}
	
	public static void setDirtyTrace(boolean dt) {
		trace = dt;
	}

	public static boolean isTrace(String name) {
		dirtyLog("Ask for trace", name);
//		String value = System.getProperty(name+".trace");
//		if (value != null) return "true".equals(value);
		return get().isTrace(name);
	}
		
	public static void doStartTrailLog(String source) {
		LevelMapper mapper = get().getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper)
			((TrailLevelMapper)mapper).doConfigureTrail(source, MLogUtil.MAP_LABEL);
	}
	
	public static void doStopTrailLog() {
		LevelMapper mapper = get().getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper)
			((TrailLevelMapper)mapper).doResetTrail();
	}

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

	public static IConfig getCfg(Object owner, IConfig def) {
		return get().getCfgManager().getCfg(owner, def);
	}
	
	/**
	 * Returns the config or an empty config as default.
	 * 
	 * @param owner
	 * @return the config
	 */
	public static IConfig getCfg(Object owner) {
		if (emptyConfig == null) emptyConfig = new HashConfig();
		return get().getCfgManager().getCfg(owner, emptyConfig);
	}
	
	public static synchronized UpdaterCfg getCfgUpdater() {
		if (configUpdater == null)
			configUpdater = new UpdaterCfg();
		return configUpdater;
	}
	
	public static File getFile(SCOPE scope, String path) {
		return get().getFile(scope, path);
	}

	public static <T> T lookup(Class<T> class1) {
		return get().getBaseControl().base().lookup(class1);
	}

	public static <T,D extends T> T lookup(Class<T> class1, Class<D> def) {
		return get().getBaseControl().base().lookup(class1, def);
	}

	public static <T extends Object> T waitFor(Class<? extends T> ifc, long timeout) {
		long start = System.currentTimeMillis();
		while (true) {
			try {
				T api = lookup(ifc);
				if (api != null) // should not happen
					return api;
			} catch (Throwable t) {}
			if (System.currentTimeMillis() - start > timeout)
				throw new TimeoutRuntimeException("timeout getting API",ifc);
			MThread.sleep(500);
		}
	}
	
	
	public static void dirtyLog(Object ... string) {
		if (string == null || !isDirtyTrace()) return;
		out .println("--- " + Arrays.toString(string));
		for (Object s : string)
			if (s instanceof Throwable)
				((Throwable)s).printStackTrace(out);
	}
		
    public static void dirtyLogInfo(Object ... string) {
        if (string == null) return;
        out .println("--- " + Arrays.toString(string));
        for (Object s : string)
            if (s instanceof Throwable)
                ((Throwable)s).printStackTrace(out);
    }
    
    public static void dirtyLogError(Object ... string) {
        if (string == null) return;
        err .println("*** " + Arrays.toString(string));
        for (Object s : string)
            if (s instanceof Throwable)
                ((Throwable)s).printStackTrace(err);
    }
    
}
