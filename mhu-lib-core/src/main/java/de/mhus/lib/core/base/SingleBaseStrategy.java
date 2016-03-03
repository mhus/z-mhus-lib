package de.mhus.lib.core.base;

import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.lang.Base;

/**
 * <p>SingleBaseStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class SingleBaseStrategy extends BaseFindStrategy {
	
	private Base defaultBase = MSingleton.get().getBaseControl().createBase(null, null);
	
	/** {@inheritDoc} */
	@Override
	public Base find(Object... attributes) {
//		Thread.currentThread().
		return defaultBase;
	}

	/** {@inheritDoc} */
	@Override
	public Base install(Base base) {
		Base cur = defaultBase;
//		defaultBase = base; // do not overwrite
		return cur;
	}
	
}
