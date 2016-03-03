package de.mhus.lib.core.cast;

/**
 * <p>Caster interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Caster<F,T> {

	/**
	 * <p>getToClass.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	public Class<? extends T> getToClass();

	/**
	 * <p>getFromClass.</p>
	 *
	 * @return a {@link java.lang.Class} object.
	 */
	public Class<? extends F> getFromClass();
		
	/**
	 * <p>cast.</p>
	 *
	 * @param in a F object.
	 * @param def a T object.
	 * @return a T object.
	 */
	public T cast(F in, T def);
	
}
