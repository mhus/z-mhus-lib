package de.mhus.lib.core.definition;

import de.mhus.lib.errors.MException;

public interface IDefTransformer {

	IDefDefinition transform(IDefDefinition component) throws MException ;

}
