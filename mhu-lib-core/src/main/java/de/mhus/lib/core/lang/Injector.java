package de.mhus.lib.core.lang;

/**
 * <p>Injector interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface Injector {

	/**
	 * <p>doInject.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @throws java.lang.Exception if any.
	 */
	void doInject(Object obj) throws Exception;
	
}
