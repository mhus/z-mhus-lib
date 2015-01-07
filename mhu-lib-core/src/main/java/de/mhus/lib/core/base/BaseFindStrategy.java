package de.mhus.lib.core.base;

import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.strategy.FindStrategy;

public abstract class BaseFindStrategy implements FindStrategy<Base> {

	@Override
	public abstract Base find(Object... attributes);
	
	public abstract Base install(Base base);
	
}
