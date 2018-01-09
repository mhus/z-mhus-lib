package de.mhus.lib.karaf.util;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class OsgiBundleClassLoader extends ClassLoader {

	
    private String loadedFrom;

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
        {
			loadedFrom = FrameworkUtil.getBundle(OsgiBundleClassLoader.class).getSymbolicName();
    		for (Bundle bundle : FrameworkUtil.getBundle(OsgiBundleClassLoader.class).getBundleContext().getBundles()) {
    			try {
    				Class<?> clazz = bundle.loadClass(name);
    				if (clazz != null) {
	    				loadedFrom = bundle.getSymbolicName();
	    				return clazz;
    				}
    			} catch (Throwable t) {
    			}
    		}
    		return super.loadClass(name, resolve);
        }

	public Map<String,Class<?>> loadAllClasses(String name)
        {
		HashMap<String,Class<?>> out = new HashMap<>();
			loadedFrom = FrameworkUtil.getBundle(OsgiBundleClassLoader.class).getSymbolicName();
    		for (Bundle bundle : FrameworkUtil.getBundle(OsgiBundleClassLoader.class).getBundleContext().getBundles()) {
    			try {
    				Class<?> clazz = bundle.loadClass(name);
    				if (clazz != null) {
    					out.put(bundle.getSymbolicName(), clazz);
    				}
    			} catch (Throwable t) {
    			}
    		}
    		return out;
        }
	
	public String getLoadBundle() {
		return loadedFrom;
	}

}
