package de.mhus.lib.karaf.adb;

public interface DbManagerAdmin {

	void addService(DbManagerService service) throws Exception;
	void removeService(DbManagerService service);
	
	DbManagerService[] getServices();

	DbManagerService getService(String name);

}
