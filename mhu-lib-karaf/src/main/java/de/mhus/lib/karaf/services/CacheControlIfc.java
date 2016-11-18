package de.mhus.lib.karaf.services;

import de.mhus.lib.basics.Named;

public interface CacheControlIfc extends Named {
	
	long getSize();
	String getName();
	void clear();
	
}
