package de.mhus.lib.core.logging;

import de.mhus.lib.annotations.activator.DefaultImplementation;
import de.mhus.lib.core.system.SimpleMLogFactory;

//@DefaultImplementation(SingleMLogInstanceFactory.class)
@DefaultImplementation(SimpleMLogFactory.class)
public interface MLogFactory {

	Log lookup(Object owner);

	void update();

}
