package de.mhus.lib.karaf.services;

import de.mhus.lib.karaf.MOsgi;

public class CacheControlUtil {

	public static void clear(String name) {
		for (CacheControlIfc c : MOsgi.getServices(CacheControlIfc.class, null))
			if (name == null || name.equals(c.getName()))
				c.clear();
	}

}
