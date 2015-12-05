package de.mhus.lib.core.system;

import java.io.File;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.lang.BaseControl;
import de.mhus.lib.core.logging.LogFactory;

public interface ISingleton {

	//Log createLog(Object owner);

	ConfigManager getConfigManager();

	BaseControl getBaseControl();

	MActivator createActivator();

	LogFactory getLogFactory();
	
	boolean isTrace(String name);
	
	Base base();

	void reConfigure();

	/**
	 * Return a File inside the current application context.
	 * 
	 * @param dir
	 * @return
	 */
	File getFile(String dir);
}
