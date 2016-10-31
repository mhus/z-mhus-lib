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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.cfg.CfgBoolean;
import de.mhus.lib.core.cfg.CfgLong;
import de.mhus.lib.core.config.HashConfig;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.core.service.UniqueId;
import de.mhus.lib.errors.MException;

/**
 * The pool handles a bundle of connections. The connections should have the same
 * credentials (url, user access). Unused or closed connections will be freed after a
 * pending period.
 * 
 * @author mikehummel
 *
 */
@JmxManaged(descrition="Database pool")
public abstract class DbPool extends MJmx {

	// Trace parameters
	private Map<String, ConnectionTrace> stackTraces = new HashMap<>();
	private long lastStackTracePrint = 0;
	private CfgBoolean traceCaller = new CfgBoolean(DbConnection.class, "traceCallers", false) {
		@Override
		protected void onPreUpdate(Boolean newValue) {
			if (stackTraces != null)
				stackTraces.clear();
		}
	};
	private CfgLong traceWait = new CfgLong(DbConnection.class, "traceCallersWait", MTimeInterval.MINUTE_IN_MILLISECOUNDS * 10);
	
	private DbProvider provider;
	private String name;
	private IConfig config;


	/**
	 * Create a new pool from central configuration.
	 * It's used the MSingleton configuration with the key of this class.
	 * 
	 * @throws Exception
	 */
	public DbPool() throws Exception {
		this(null,null);
	}

	/**
	 * Create a new pool from a configuration.
	 * 
	 * @param config Config element or null. null will use the central MSingleton configuration.
	 * @param activator Activator or null. null will use the central MSingleton Activator.
	 * @throws Exception
	 */
	public DbPool(IConfig config,MActivator activator) throws Exception {

		this.config = config;

		if (this.config == null) doCreateConfig();
		if (activator == null) activator = MSingleton.baseLookup(this,MActivator.class);

		DbProvider provider = (DbProvider) activator.createObject(this.config.getExtracted("provider",JdbcProvider.class.getCanonicalName()));
		provider.doInitialize(this.config,activator);

		this.provider = provider;
	}

	/**
	 * Create a pool with the DbProvider.
	 * 
	 * @param provider
	 */
	public DbPool(DbProvider provider) {
		doCreateConfig();
		setProvider(provider);
	}

	protected IConfig getConfig() {
		return config;
	}

	protected String getName() {
		return name;
	}

	protected void doCreateConfig() {
		try {
			config = MSingleton.get().getCfgManager().getCfg(this,null);
		} catch (Throwable t) {
		}
		if (config == null) config = new HashConfig();
	}

	/**
	 * Set a DbProvider for this pool.
	 * 
	 * @param provider
	 */
	protected void setProvider(DbProvider provider) {
		this.provider = provider;
		name = provider.getName();
		if (name == null) name = "pool";
		name = name + MSingleton.baseLookup(this,UniqueId.class).nextUniqueId();
	}

	/**
	 * Returns the DbProvider, it implements the database behavior and creates new connections.
	 * 
	 * @return x
	 */
	public DbProvider getProvider() {
		return provider;
	}

	/**
	 * Returns the database dialect object. (Delegated to DbProvider).
	 * @return x
	 */
	public Dialect getDialect() {
		return provider.getDialect();
	}

	/**
	 * Look into the pool for an unused DbProvider. If no one find, create one.
	 * 
	 * @return x
	 * @throws Exception
	 */
	public abstract DbConnection getConnection() throws Exception;

	/**
	 * Current pool size.
	 * 
	 * @return x Current pool size, also pending closed connections.
	 */
	@JmxManaged(descrition="Current size of the pool")
	public abstract int getSize();

	@JmxManaged(descrition="Current used connections in the pool")
	public abstract int getUsedSize();

	/**
	 * Cleanup the connection pool. Unused or closed connections will be removed.
	 * TODO new strategy to remove unused connections - not prompt, need a timeout time or minimum pool size.
	 * @param unusedAlso 
	 */
	@JmxManaged(descrition="Cleanup unused connections")
	public abstract void cleanup(boolean unusedAlso);

	/**
	 * Close the pool and all connections.
	 * 
	 */
	public abstract void close();

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	public DbPrepared getStatement(String name) throws MException {
		String[] query = provider.getQuery(name);
		return new DbPrepared(this, query[1], query[0]);
	}

	/**
	 * Create a prepared statement using the default language.
	 * 
	 * @param sql
	 * @return x
	 * @throws MException
	 */
	public DbPrepared createStatement(String sql) throws MException {
		return createStatement(sql, null);
	}

	/**
	 * Create a new prepared statement for further use.
	 * 
	 * @param sql
	 * @param language
	 * @return x
	 * @throws MException
	 */
	public DbPrepared createStatement(String sql, String language) throws MException {
		return new DbPrepared(this, sql, language);
	}

	@JmxManaged(descrition="Unique name of the pool")
	public String getPoolId() {
		return name;
	}

	@JmxManaged(descrition="Return the usage of the connections")
	public abstract String dumpUsage(boolean used);

	public abstract boolean isClosed();

	public Map<String,ConnectionTrace> getStackTraces() {
		return stackTraces ;
	}
	
	public void printStackTrace() {
		if (traceCaller.value() && lastStackTracePrint + traceWait.value() < System.currentTimeMillis()) {
			lastStackTracePrint = System.currentTimeMillis();
			LinkedList<ConnectionTrace> list = new LinkedList<ConnectionTrace>(getStackTraces().values());
			Collections.sort(list);
			log().f("Connection Usage",list.size());
			for (ConnectionTrace trace :list) {
				trace.log(log());
			}
		}
	}

}
