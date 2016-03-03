package de.mhus.lib.core.pojo;

/**
 * <p>PojoFilter interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface PojoFilter {

	/**
	 * <p>filter.</p>
	 *
	 * @param model a {@link de.mhus.lib.core.pojo.PojoModelImpl} object.
	 */
	void filter(PojoModelImpl model);

}
