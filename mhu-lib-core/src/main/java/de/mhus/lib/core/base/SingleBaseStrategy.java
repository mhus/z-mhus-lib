package de.mhus.lib.core.base;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.lang.Base;

public class SingleBaseStrategy extends BaseFindStrategy {
	
	private Base defaultBase = MApi.get().getBaseControl().createBase(null, null);
	
	@Override
	public Base find(Object... attributes) {
//		Thread.currentThread().
		return defaultBase;
	}

	@Override
	public Base install(Base base) {
		Base cur = defaultBase;
//		defaultBase = base; // do not overwrite
		return cur;
	}
	
}
