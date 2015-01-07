package de.mhus.lib.karaf.adb;

import de.mhus.lib.adb.DbManager;

public interface DbManagerService {

	void updateManager(boolean clean) throws Exception;

	DbManager getManager();
	
	boolean isConnected();
	
	String getDataSourceName();
	
	void setDataSourceName(String dataSourceName);

	String getServiceName();

}
