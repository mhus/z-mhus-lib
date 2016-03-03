package de.mhus.lib.core.base;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.lang.Base;

/**
 * <p>BaseByThreadStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class BaseByThreadStrategy extends BaseFindStrategy {

	private Base defaultBase = MSingleton.get().getBaseControl().createBase(null, null);
	
	private ThreadLocal<Base> threadBase = new ThreadLocal<Base>() {
		@Override
		protected Base initialValue() {
			return defaultBase;
		}
	};

	/** {@inheritDoc} */
	@Override
	public Base find(Object... attributes) {
//		Thread.currentThread().
		return threadBase.get();
	}

	/** {@inheritDoc} */
	@Override
	public Base install(Base base) {
		Base cur = threadBase.get();
		threadBase.set(base);
		return cur;
	}
	
}
