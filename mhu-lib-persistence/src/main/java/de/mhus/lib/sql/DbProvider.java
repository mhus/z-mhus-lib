/*
 *  Copyright (C) 2002-2004 Mike Hummel
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package de.mhus.lib.sql;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.directory.ResourceNode;
import de.mhus.lib.core.lang.MObject;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;

/**
 * Provide the database connections and other db specific stuff.
 * The default implementation is the JdbcProvider. Implement this to
 * create a completely new kind of database for the framework.
 * 
 * @author mikehummel
 *
 */
public abstract class DbProvider extends MObject {

	//	private static Log log = Log.getLog(DbProvider.class);

	protected ResourceNode config;
	protected MActivator activator;

	/**
	 * Returns a new DbConnection for this kind of database.
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract InternalDbConnection createConnection() throws Exception;

	/**
	 * Set configuration element and activator.
	 * 
	 * @param config
	 * @param activator
	 */
	public void doInitialize(ResourceNode config,MActivator activator) {
		if (config == null) config = new HashConfig();
		if (activator == null) activator = base(MActivator.class);
		this.config = config;
		this.activator = activator;
	}

	/**
	 * Returns the predefined statement by this name.
	 * TODO need to manipulate the set of statements from outside.
	 * 
	 * @param name
	 * @return The query string or null.
	 */
	public String[] getQuery(String name) {
		try {
			ResourceNode query = config.getNode("queries");
			String queryLanguage = null;
			String queryString = query.getString(name,null);
			String[] out = new String[] { queryLanguage, queryString };

			if (queryString == null) {
				for (ResourceNode q : query.getNodes("query") ) {
					if (q.getString("name", "").equals(name)) {
						queryLanguage = q.getExtracted("language");
						queryString = q.getExtracted("string");
						out = new String[] { queryLanguage, queryString };
						break;
					}
				}
			}
			return out;
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}

	/**
	 * Returns the Dialect object for this database. It contains all deep specific
	 * abstraction functions to handle the database.
	 * 
	 * @return
	 */
	public abstract Dialect getDialect();

	/**
	 * Nice name of the connection - from configuration to identify it.
	 * 
	 * @return
	 */
	public String getName() {
		try {
			return config.getExtracted("name");
		} catch (MException e) {
			throw new MRuntimeException(e);
		}
	}

	/**
	 * Returns the used activator.
	 * 
	 * @return
	 */
	public MActivator getActivator() {
		return activator;
	}

}
