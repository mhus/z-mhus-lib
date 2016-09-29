package de.mhus.lib.core.system;


import java.io.File;
import java.util.Set;

import de.mhus.lib.core.logging.LogFactory;

public interface ISingletonInternal {

	void setLogFactory(LogFactory logFactory);

	Set<String> getLogTrace();

	void setBaseDir(File file);

}
