package de.mhus.lib.core.system;

import java.io.File;

import de.mhus.lib.core.MConstants;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cfg.CfgInitiator;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * <p>SystemCfgInitiator class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class SystemCfgInitiator implements CfgInitiator {

	/** {@inheritDoc} */
	@Override
	public void doInitialize(ISingletonInternal internal, CfgManager manager) {
		ResourceNode system = manager.getCfg("system");
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
