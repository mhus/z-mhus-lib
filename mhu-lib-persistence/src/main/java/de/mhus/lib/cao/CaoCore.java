package de.mhus.lib.cao;

import java.util.HashMap;

import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.cao.util.MutableActionList;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public abstract class CaoCore extends CaoConnection {

	protected CaoDriver driver;
	protected MutableActionList actionList = new MutableActionList();
	protected HashMap<Class<? extends CaoAspect>,CaoAspectFactory<?>> aspectFactory = new HashMap<>();
	protected String name;
	protected CaoConnection con;

	public CaoCore(String name, CaoDriver driver) {
		this.name = name;
		this.driver = driver;
	}

	
	public CaoConnection getConnection() {
		return con;
	}
	
	public CaoDriver getDriver() {
		return driver;
	}

	public CaoActionList getActions() {
		return actionList;
	}
		
	public <T extends CaoAspect> void registerAspectFactory(Class<T> ifc,CaoAspectFactory<T> factory) throws MException {
		if (aspectFactory.containsKey(ifc))
			throw new MException("Aspect already registered",ifc);
		aspectFactory.put(ifc, factory);
		factory.doInitialize(this, actionList);
	}

	public void doInitializeActions() {
		for (CaoAction action : actionList)
			action.doInitialize(this);
	}

	@SuppressWarnings("unchecked")
	public <T extends CaoAspect> CaoAspectFactory<T> getAspectFactory(Class<T> ifc) {
		return (CaoAspectFactory<T>)aspectFactory.get(ifc);
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Send a query into the data store system. This method opens the possibility to
	 * access more then the content structure. But it's not specified and must be
	 * known by the caller.
	 *  e.g. get access to the underlying type or user system or send proprietary queries
	 *  to the system.
	 * 
	 * @param space The data space, e.g. users, types, content
	 * @param query The query itself
	 * @return A list of results.
	 * @throws MException Throws NotSupportedException if the method is not implemented at all
	 */
	public CaoList executeQuery(String space, String query) throws MException {
		throw new NotSupportedException();
	}

	/**
	 * 
	 * Send a query into the data store system. This method opens the possibility to
	 * access more then the content structure. But it's not specified and must be
	 * known by the caller.
	 *  e.g. get access to the underlying type or user system or send proprietary queries
	 *  to the system.
	 *  
	 * @param space The data space, e.g. users, types, content
	 * @param query The query itself
	 * @return A list of results.
	 * @throws MException Throws NotSupportedException if the method is not implemented at all
	 */
	@Override
	public <T> CaoList executeQuery(String space, AQuery<T> query) throws MException {
		// return executeQuery(space, query.toQualification(dbManager) );
		throw new NotSupportedException();
	}

	@Override
	public CaoAction getAction(String name) {
		return getActions().getAction(name);
	}

	@Override
	public boolean containsNode(CaoNode node) {
		return con.equals(node.getConnection());
	}

}
