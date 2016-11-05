package de.mhus.lib.cao;

import java.util.HashMap;

import de.mhus.lib.cao.util.MutableActionList;
import de.mhus.lib.core.directory.MResourceProvider;
import de.mhus.lib.errors.MException;

public abstract class CaoConnection extends MResourceProvider<CaoNode> {

	protected CaoDriver driver;
	protected MutableActionList actionList = new MutableActionList();
	protected HashMap<Class<? extends CaoAspect>,CaoAspectFactory<?>> aspectFactory = new HashMap<>();
	protected String name;

	public CaoConnection(String name, CaoDriver driver) {
		this.driver = driver;
		this.name = name;
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
		
	public <T extends CaoAspect> void registerAspectFactory(Class<T> ifc,CaoAspectFactory<T> factory) throws MException {
		if (aspectFactory.containsKey(ifc))
			throw new MException("Aspect already registered",ifc);
		aspectFactory.put(ifc, factory);
		factory.doInitialize(this, actionList);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends CaoAspect> CaoAspectFactory<T> getAspectFactory(Class<T> ifc) {
		return (CaoAspectFactory<T>)aspectFactory.get(ifc);
	}

	@Override
	public String getName() {
		return name;
	}
	
}
