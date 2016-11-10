package de.mhus.lib.cao;

import de.mhus.lib.cao.util.MutableActionList;

public interface CaoAspectFactory<T extends CaoAspect> {

	T getAspectFor(CaoNode node);

	void doInitialize(CaoCore core, MutableActionList actionList);
	
}
