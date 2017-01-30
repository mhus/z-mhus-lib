package de.mhus.lib.cao.aspect;

import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoNode;

public interface Changes extends CaoAspect {

	void deleted();
	void modified();
	void created();
	void moved();
	void movedFrom(CaoNode oldParent);
	
}
