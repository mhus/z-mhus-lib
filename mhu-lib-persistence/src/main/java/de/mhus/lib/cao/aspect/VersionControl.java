package de.mhus.lib.cao.aspect;

import de.mhus.lib.cao.CaoAspect;

public interface VersionControl extends CaoAspect {

	boolean commit();
	boolean revert();
	boolean update();
	boolean isChanged();
}
