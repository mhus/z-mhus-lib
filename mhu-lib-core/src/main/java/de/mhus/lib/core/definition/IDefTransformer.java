package de.mhus.lib.core.definition;

import de.mhus.lib.errors.MException;

/**
 * <p>IDefTransformer interface.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public interface IDefTransformer {

	/**
	 * <p>transform.</p>
	 *
	 * @param component a {@link de.mhus.lib.core.definition.IDefDefinition} object.
	 * @return a {@link de.mhus.lib.core.definition.IDefDefinition} object.
	 * @throws de.mhus.lib.errors.MException if any.
	 */
	IDefDefinition transform(IDefDefinition component) throws MException ;

}
