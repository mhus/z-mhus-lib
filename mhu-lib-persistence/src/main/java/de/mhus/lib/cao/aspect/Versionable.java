package de.mhus.lib.cao.aspect;

import java.util.Set;

import de.mhus.lib.cao.CaoAspect;
import de.mhus.lib.cao.CaoNode;
import de.mhus.lib.errors.MException;

public interface Versionable extends CaoAspect {

	public String getVersionLabel() throws MException;

	public Set<String> getVersions() throws MException;

	public CaoNode getVersion(String version) throws MException;

}
