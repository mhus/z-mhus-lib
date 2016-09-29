package de.mhus.lib.core.lang;

import java.net.URL;

public class EmptyClassLoader extends ClassLoader {

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		throw new ClassNotFoundException(name);
	}

	@Override
	public URL getResource(String name) {
		return null;
	}

	
}
