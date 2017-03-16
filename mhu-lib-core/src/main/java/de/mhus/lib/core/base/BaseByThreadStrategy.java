package de.mhus.lib.core.base;

import de.mhus.lib.core.MApi;
import de.mhus.lib.core.lang.Base;

public class BaseByThreadStrategy extends BaseFindStrategy {

	private Base defaultBase = MApi.get().getBaseControl().createBase(null, null);
	
	private ThreadLocal<Base> threadBase = new ThreadLocal<Base>() {
		@Override
		protected Base initialValue() {
			return defaultBase;
		}
	};

	@Override
	public Base find(Object... attributes) {
//		Thread.currentThread().
		return threadBase.get();
	}

	@Override
	public Base install(Base base) {
		Base cur = threadBase.get();
		threadBase.set(base);
		return cur;
	}
	
}
