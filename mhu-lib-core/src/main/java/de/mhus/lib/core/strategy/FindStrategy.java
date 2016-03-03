package de.mhus.lib.core.strategy;

/**
 * <p>FindStrategy interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface FindStrategy<T> {

	/**
	 * <p>find.</p>
	 *
	 * @param attributes a {@link java.lang.Object} object.
	 * @return a T object.
	 */
	public T find(Object ... attributes);
	
}
