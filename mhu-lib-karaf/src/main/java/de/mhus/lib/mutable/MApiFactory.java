package de.mhus.lib.mutable;

import de.mhus.lib.core.system.IApi;
import de.mhus.lib.core.system.IApiFactory;

public class MApiFactory implements IApiFactory {

	@Override
	public IApi createApi() {
		return new KarafMApiImpl();
	}

}
