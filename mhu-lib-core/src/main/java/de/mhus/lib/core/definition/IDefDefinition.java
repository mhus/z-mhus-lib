package de.mhus.lib.core.definition;

import de.mhus.lib.errors.MException;

public interface IDefDefinition {

	void inject(DefComponent root) throws MException;

}
