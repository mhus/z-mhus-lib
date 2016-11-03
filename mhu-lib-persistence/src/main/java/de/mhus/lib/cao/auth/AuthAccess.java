package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoNode;

public interface AuthAccess extends CaoAspect {

	boolean hasReadAccess();

	boolean hasWriteAccess();

	boolean hasContentAccess(String rendition);

	boolean hasAspectAccess(Class<? extends CaoAspect> ifc);

}
