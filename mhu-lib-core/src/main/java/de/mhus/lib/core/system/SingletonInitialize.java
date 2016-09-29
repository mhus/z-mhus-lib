package de.mhus.lib.core.system;

/**
 * <p>SingletonInitialize interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface SingletonInitialize {

	/**
	 * <p>doInitialize.</p>
	 *
	 * @param coreLoader a {@link java.lang.ClassLoader} object.
	 */
	void doInitialize(ClassLoader coreLoader);

}
