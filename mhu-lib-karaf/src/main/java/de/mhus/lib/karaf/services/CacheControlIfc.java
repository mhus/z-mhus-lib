package de.mhus.lib.karaf.services;

import de.mhus.lib.basics.Named;

public interface CacheControlIfc extends Named {
	
	long getSize();
	@Override
	String getName();
	void clear();
	boolean isEnabled();
	void setEnabled(boolean enabled);
	
}
