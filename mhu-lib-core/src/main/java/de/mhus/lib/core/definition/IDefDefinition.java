package de.mhus.lib.core.definition;

import de.mhus.lib.errors.MException;

/**
 * <p>IDefDefinition interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface IDefDefinition {

	/**
	 * <p>inject.</p>
	 *
	 * @param root a {@link de.mhus.lib.core.definition.DefComponent} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	void inject(DefComponent root) throws MException;

}
