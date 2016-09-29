package de.mhus.lib.core.pojo;

/**
 * <p>PojoStrategy interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface PojoStrategy {

	
	/**
	 * <p>parseObject.</p>
	 *
	 * @param parser a {@link de.mhus.lib.core.pojo.PojoParser} object.
	 * @param pojo a {@link java.lang.Object} object.
	 * @param model a {@link de.mhus.lib.core.pojo.PojoModelImpl} object.
	 */
	void parseObject(PojoParser parser, Object pojo, PojoModelImpl model);
	
	/**
	 * <p>parse.</p>
	 *
	 * @param parser a {@link de.mhus.lib.core.pojo.PojoParser} object.
	 * @param clazz a {@link java.lang.Class} object.
	 * @param model a {@link de.mhus.lib.core.pojo.PojoModelImpl} object.
	 */
	void parse(PojoParser parser, Class<?> clazz, PojoModelImpl model);
	
}
