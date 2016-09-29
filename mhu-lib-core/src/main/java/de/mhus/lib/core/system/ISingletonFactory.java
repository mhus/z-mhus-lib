package de.mhus.lib.core.system;

/**
 * <p>ISingletonFactory interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface ISingletonFactory {

	/**
	 * <p>createSingleton.</p>
	 *
	 * @return a {@link de.mhus.lib.core.system.ISingleton} object.
	 */
	ISingleton createSingleton();
	
}
