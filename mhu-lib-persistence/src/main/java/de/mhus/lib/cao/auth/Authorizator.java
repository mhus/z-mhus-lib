package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoNode;

public interface Authorizator {

	boolean hasReadAccess(CaoNode node);

	boolean hasWriteAccess(CaoNode node);

	boolean hasActionAccess(CaoAction action);

	boolean hasReadAccess(CaoNode node, String name);

	boolean hasContentAccess(CaoNode node, String rendition);

	boolean hasWriteAccess(CaoNode node, String name);

	boolean hasAspectAccess(CaoNode node, Class<? extends CaoAspect> ifc);

}
