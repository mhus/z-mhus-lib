package de.mhus.lib.core.system;

import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.MLogFactory;

public class SimpleMLogFactory implements MLogFactory {

	@Override
	public Log lookup(Object owner) {
		return new Log(owner);
	}

	@Override
	public void update() {
		// not supported
	}

}
