package de.mhus.lib.core;

import java.io.File;
import java.util.UUID;
import java.util.WeakHashMap;

import de.mhus.lib.core.cfg.UpdaterCfg;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.logging.LevelMapper;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.TrailLevelMapper;
import de.mhus.lib.core.service.UniqueId;
import de.mhus.lib.core.system.DefaultSingleton;
import de.mhus.lib.core.system.DummyClass;
import de.mhus.lib.core.system.ISingleton;
import de.mhus.lib.core.system.ISingletonFactory;
import de.mhus.lib.core.system.SingletonInitialize;

/**
 * <p>MSingleton class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MSingleton {

	private static ISingleton singleton;
	/** Constant <code>trace</code> */
	protected static Boolean trace;
	private static WeakHashMap<UUID, Log> loggers = new WeakHashMap<>();
	private static ResourceNode emptyConfig = null;
	private static UpdaterCfg configUpdater;
	
//	private static DummyClass dummy = new DummyClass(); // the class is inside this bundle and has the correct class loader
	
	private MSingleton() {}
	
	/**
	 * <p>get.</p>
	 *
	 * @return a {@link de.mhus.lib.core.system.ISingleton} object.
	 */
	public static synchronized ISingleton get() {
		if (singleton == null) {
			try {
				ISingleton obj = null;
				String path = "de.mhus.lib.mutable.SingletonFactory";
				if (System.getProperty("mhu.lib.singleton.factory") != null) path = System.getProperty(MConstants.PROP_SINGLETON_FACTORY_CLASS);
				if (isDirtyTrace()) System.out.println("--- MSingletonFactory:" + path);
				ISingletonFactory factory = (ISingletonFactory)Class.forName(path).newInstance();
				if (factory != null) {
					obj = factory.createSingleton();
				}
				singleton = obj;
			} catch (Throwable t) {
				if (isDirtyTrace()) t.printStackTrace();
			}
			if (singleton == null)
				singleton = new DefaultSingleton();
			if (isDirtyTrace()) System.out.println("--- MSingleton: " + singleton.getClass().getCanonicalName());
			if (singleton instanceof SingletonInitialize)
				((SingletonInitialize)singleton).doInitialize(DummyClass.class.getClassLoader());
		}
		return singleton;
	}
	
	
	/**
	 * <p>isDirtyTrace.</p>
	 *
	 * @return a boolean.
	 */
	public static boolean isDirtyTrace() {
		if (trace == null) trace = "true".equals(System.getProperty(MConstants.PROP_DIRTY_TRACE));
		return trace;
	}
	
	/**
	 * <p>setDirtyTrace.</p>
	 *
	 * @param dt a boolean.
	 */
	public static void setDirtyTrace(boolean dt) {
		trace = dt;
	}

	/**
	 * <p>isTrace.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public static boolean isTrace(String name) {
		if (isDirtyTrace()) 
			System.out.println("--- Ask for trace: " + name);
//		String value = System.getProperty(name+".trace");
//		if (value != null) return "true".equals(value);
		return get().isTrace(name);
	}
		
	/**
	 * <p>doStartTrailLog.</p>
	 */
	public static void doStartTrailLog() {
		LevelMapper mapper = get().getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper)
			((TrailLevelMapper)mapper).doConfigureTrail("MAP");
	}
	
	/**
	 * <p>doStopTrailLog.</p>
	 */
	public static void doStopTrailLog() {
		LevelMapper mapper = get().getLogFactory().getLevelMapper();
		if (mapper != null && mapper instanceof TrailLevelMapper)
			((TrailLevelMapper)mapper).doResetTrail();
	}

	/**
	 * <p>registerLogger.</p>
	 *
	 * @param log a {@link de.mhus.lib.core.logging.Log} object.
	 */
	public static void registerLogger(Log log) {
		synchronized (loggers) {
			loggers.put(log.getId(), log);
		}
	}

	/**
	 * <p>unregisterLogger.</p>
	 *
	 * @param log a {@link de.mhus.lib.core.logging.Log} object.
	 */
	public static void unregisterLogger(Log log) {
		synchronized (loggers) {
			loggers.remove(log.getId());
		}
	}
	
	/**
	 * <p>updateLoggers.</p>
	 */
	public static void updateLoggers() {
		try {
			synchronized (loggers) {
				for (UUID logId : loggers.keySet().toArray(new UUID[loggers.size()]))
					loggers.get(logId).update();
			}
		} catch(Throwable t) {
			if (MSingleton.isDirtyTrace()) t.printStackTrace();
		}
	}

	/**
	 * <p>getCfg.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param def a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @return a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @since 3.3.0
	 */
	public static ResourceNode getCfg(Object owner, ResourceNode def) {
		return get().getCfgManager().getCfg(owner, def);
	}
	
	/**
	 * Returns the config or an empty config as default.
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @return a {@link de.mhus.lib.core.directory.ResourceNode} object.
	 * @since 3.3.0
	 */
	public static ResourceNode getCfg(Object owner) {
		if (emptyConfig == null) emptyConfig = new HashConfig();
		return get().getCfgManager().getCfg(owner, emptyConfig);
	}
	
	/**
	 * <p>getCfgUpdater.</p>
	 *
	 * @return a {@link de.mhus.lib.core.cfg.UpdaterCfg} object.
	 * @since 3.3.0
	 */
	public static synchronized UpdaterCfg getCfgUpdater() {
		if (configUpdater == null)
			configUpdater = new UpdaterCfg();
		return configUpdater;
	}
	
	/**
	 * <p>getFile.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @return a {@link java.io.File} object.
	 * @since 3.3.0
	 */
	public static File getFile(String path) {
		return get().getFile(path);
	}

	/**
	 * <p>baseLookup.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param class1 a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	public static <T> T baseLookup(Object owner, Class<T> class1) {
		return get().getBaseControl().base(owner).lookup(class1);
	}

	/**
	 * <p>dirtyLog.</p>
	 *
	 * @param string a {@link java.lang.Object} object.
	 */
	public static void dirtyLog(Object ... string) {
		if (isDirtyTrace())
			System.out.println(string);
	}
		
}
