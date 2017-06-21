package de.mhus.lib.core.system;

import java.io.File;

import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MApi;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.directory.ResourceNode;

public class SystemCfgInitiator implements CfgInitiator {

	@Override
	public void doInitialize(IApiInternal internal, CfgManager manager) {
		ResourceNode system = manager.getCfg("system");
		try {
			String key = MConstants.PROP_BASE_DIR;
			String name = system.getString(key);
			if (MString.isEmpty(name)) name = System.getProperty(MConstants.PROP_PREFIX + key);
			String baseDirName = ".";
			if (MString.isSet(name)) 
				baseDirName = name;
			internal.setBaseDir( new File(baseDirName) );
		} catch (Throwable t) {if (MApi.isDirtyTrace()) t.printStackTrace();}
	}

}
