package de.mhus.lib.core.system;

import java.io.File;

import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.directory.ResourceNode;

public class SystemConfigInitiator implements ConfigInitiator {

	@Override
	public void doInitialize(ISingletonInternal internal, ConfigManager manager) {
		ResourceNode system = manager.getConfig("system");
		try {
			String key = MConstants.PROP_BASE_DIR;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			String baseDirName = ".";
			if (MString.isSet(name)) 
				baseDirName = name;
			internal.setBaseDir( new File(baseDirName) );
		} catch (Throwable t) {if (MSingleton.isDirtyTrace()) t.printStackTrace();}	
	}

}
