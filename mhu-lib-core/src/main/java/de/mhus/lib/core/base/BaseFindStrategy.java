package de.mhus.lib.core.base;

import de.mhus.lib.core.lang.Base;
import de.mhus.lib.core.strategy.FindStrategy;

/**
 * <p>Abstract BaseFindStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class BaseFindStrategy implements FindStrategy<Base> {

	/** {@inheritDoc} */
	@Override
	public abstract Base find(Object... attributes);
	
	/**
	 * <p>install.</p>
	 *
	 * @param base a {@link de.mhus.lib.core.lang.Base} object.
	 * @return a {@link de.mhus.lib.core.lang.Base} object.
	 */
	public abstract Base install(Base base);
	
}
