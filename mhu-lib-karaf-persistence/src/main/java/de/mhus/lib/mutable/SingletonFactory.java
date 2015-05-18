package de.mhus.lib.mutable;

import de.mhus.lib.core.system.ISingleton;
import de.mhus.lib.core.system.ISingletonFactory;

public class SingletonFactory implements ISingletonFactory {

	@Override
	public ISingleton createSingleton() {
		return new KarafSingletonImpl();
	}

}
