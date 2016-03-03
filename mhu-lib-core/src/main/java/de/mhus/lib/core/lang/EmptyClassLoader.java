package de.mhus.lib.core.lang;

import java.net.URL;

/**
 * <p>EmptyClassLoader class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class EmptyClassLoader extends ClassLoader {

	/** {@inheritDoc} */
	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		throw new ClassNotFoundException(name);
	}

	/** {@inheritDoc} */
	@Override
	public URL getResource(String name) {
		return null;
	}

	
}
