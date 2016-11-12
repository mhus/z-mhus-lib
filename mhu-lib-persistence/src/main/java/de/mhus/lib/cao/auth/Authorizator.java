package de.mhus.lib.cao.auth;

import java.util.Collection;

import de.mhus.lib.cao.CaoAction;
import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.cao.action.CaoConfiguration;

public interface Authorizator {

	boolean hasReadAccess(CaoNode node);

	boolean hasWriteAccess(CaoNode node);

	boolean hasActionAccess(CaoAction action);

	boolean hasReadAccess(CaoNode node, String name);

	boolean hasContentAccess(CaoNode node, String rendition);

	boolean hasWriteAccess(CaoNode node, String name);

	boolean hasAspectAccess(CaoNode node, Class<? extends CaoAspect> ifc);

	String mapReadName(CaoNode node, String name);

	String mapReadRendition(CaoNode node, String rendition);

	Collection<String> mapReadNames(CaoNode node, Collection<String> set);

	String mapWriteName(CaoNode node, String name);

	boolean hasActionAccess(CaoConfiguration configuration, CaoAction action);

}
