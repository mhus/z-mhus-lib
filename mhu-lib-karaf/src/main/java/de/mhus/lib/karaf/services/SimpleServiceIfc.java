package de.mhus.lib.karaf.services;

public interface SimpleServiceIfc {

	String getSimpleServiceInfo();
	String getSimpleServiceStatus();
	void setSimpleServiceConfiguration(String in);
	
}
