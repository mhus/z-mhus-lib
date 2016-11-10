package de.mhus.lib.cao;

import java.util.HashMap;

import de.mhus.lib.adb.query.AQuery;
import de.mhus.lib.basics.Named;
import de.mhus.lib.cao.util.MutableActionList;
import de.mhus.lib.core.directory.MResourceProvider;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public abstract class CaoConnection extends MResourceProvider<CaoNode> implements Named {


	public abstract CaoDriver getDriver();

	/**
	 * Request the first resource.
	 * 
	 * @return The root or null
	 */
	public abstract CaoNode getRoot();

	public abstract CaoActionList getActions();
	
	@SuppressWarnings("unchecked")
	public abstract <T extends CaoAspect> CaoAspectFactory<T> getAspectFactory(Class<T> ifc);

	@Override
	public abstract String getName();

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
	public abstract CaoList executeQuery(String space, String query) throws MException;

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
	public abstract <T> CaoList executeQuery(String space, AQuery<T> query) throws MException;

	public abstract CaoAction getAction(String name);

}
