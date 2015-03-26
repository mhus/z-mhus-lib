package de.mhus.lib.core;

import de.mhus.lib.core.system.DefaultSingleton;
import de.mhus.lib.core.system.DummyClass;
import de.mhus.lib.core.system.ISingleton;
import de.mhus.lib.core.system.ISingletonFactory;
import de.mhus.lib.core.system.SingletonInitialize;

public class MSingleton {

	private static ISingleton singleton;
	protected static Boolean trace;
	
//	private static DummyClass dummy = new DummyClass(); // the class is inside this bundle and has the correct class loader
	
	private MSingleton() {}
	
	public static synchronized ISingleton get() {
		if (singleton == null) {
			try {
				ISingleton obj = null;
				String path = "de.mhus.lib.mutable.SingletonFactory";
				if (System.getProperty("mhu.lib.singleton.factory") != null) path = System.getProperty(MSystem.PROP_SINGLETON_FACTORY_CLASS);
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
	
	
	public static boolean isDirtyTrace() {
		if (trace == null) trace = "true".equals(System.getProperty(MSystem.PROP_DIRTY_TRACE));
		return trace;
	}
	
	public static void setDirtyTrace(boolean dt) {
		trace = dt;
	}

	public static boolean isTrace(String name) {
		if (isDirtyTrace()) 
			System.out.println("--- Ask for trace: " + name);
//		String value = System.getProperty(name+".trace");
//		if (value != null) return "true".equals(value);
		return get().isTrace(name);
	}
		
}
