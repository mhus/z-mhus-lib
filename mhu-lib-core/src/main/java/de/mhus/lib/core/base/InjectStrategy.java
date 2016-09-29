package de.mhus.lib.core.base;

import de.mhus.lib.core.lang.Base;

/**
 * <p>Abstract InjectStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class InjectStrategy {

	/**
	 * <p>inject.</p>
	 *
	 * @param object a {@link java.lang.Object} object.
	 * @param base a {@link de.mhus.lib.core.lang.Base} object.
	 */
	public abstract void inject(Object object, Base base);

}
