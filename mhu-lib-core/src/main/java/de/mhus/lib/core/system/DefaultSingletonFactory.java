package de.mhus.lib.core.system;

import de.mhus.lib.annotations.activator.ObjectFactory;
import de.mhus.lib.core.MSingleton;

/**
 * <p>DefaultSingletonFactory class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class DefaultSingletonFactory implements ObjectFactory {

	/** {@inheritDoc} */
	@Override
	public Object create(Class<?> clazz, Class<?>[] classes, Object[] objects) {
		if (clazz == CfgManager.class)
			return MSingleton.get().getCfgManager();
		return null;
	}

}
