package de.mhus.lib.core.system;

import de.mhus.lib.annotations.activator.ObjectFactory;
import de.mhus.lib.core.MApi;

public class DefaultMApiFactory implements ObjectFactory {

	@Override
	public Object create(Class<?> clazz, Class<?>[] classes, Object[] objects) {
		if (clazz == CfgManager.class)
			return MApi.get().getCfgManager();
		return null;
	}

}
