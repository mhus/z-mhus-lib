package de.mhus.lib.cao;

import java.util.HashMap;

import de.mhus.lib.cao.util.MutableActionList;
import de.mhus.lib.core.directory.MResourceProvider;

public abstract class CaoConnection extends MResourceProvider<CaoNode> {

	protected CaoDriver driver;
	protected MutableActionList actionList = new MutableActionList();
	protected HashMap<Class<? extends CaoAspect>,CaoAspectFactory> aspectFactory = new HashMap<>();

	public CaoConnection(CaoDriver driver) {
		this.driver = driver;
	}

	public CaoDriver getDriver() {
		return driver;
	}

	/**
	 * Request the first resource.
	 * 
	 * @return The root or null
	 */
	public abstract CaoNode getRoot();

	public CaoActionList getActions() {
		return actionList;
	}
	
	public abstract boolean supportVersions();
	
	public void addAspectFactory(Class<? extends CaoAspect> ifc,CaoAspectFactory factory) {
		aspectFactory.put(ifc, factory);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends CaoAspectFactory> T getAspectFactory(Class<? extends CaoAspect> ifc) {
		return (T) aspectFactory.get(ifc);
	}
	
}
