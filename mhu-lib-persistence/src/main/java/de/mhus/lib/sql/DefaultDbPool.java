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

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MHousekeeper;
import de.mhus.lib.core.MHousekeeperTask;
import de.mhus.lib.core.MSingleton;
import de.mhus.lib.core.directory.ResourceNode;

/**
 * The pool handles a bundle of connections. The connections should have the same
 * credentials (url, user access). Unused or closed connections will be freed after a
 * pending period.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DefaultDbPool extends DbPool {

	private List<InternalDbConnection> pool = new LinkedList<InternalDbConnection>();


	/**
	 * Create a new pool from central configuration.
	 * It's used the MSingleton configuration with the key of this class.
	 *
	 * @throws java.lang.Exception if any.
	 */
	public DefaultDbPool() throws Exception {
		super(null,null);
		initHousekeeper();
	}

	/**
	 * Create a new pool from a configuration.
	 *
	 * @param config Config element or null. null will use the central MSingleton configuration.
	 * @param activator Activator or null. null will use the central MSingleton Activator.
	 * @throws java.lang.Exception if any.
	 */
	public DefaultDbPool(ResourceNode config,MActivator activator) throws Exception {
		super(config,activator);
		initHousekeeper();
	}

	/**
	 * Create a pool with the DbProvider.
	 *
	 * @param provider a {@link de.mhus.lib.sql.DbProvider} object.
	 */
	public DefaultDbPool(DbProvider provider) {
		super(provider);
		initHousekeeper();
	}

	/**
	 * <p>initHousekeeper.</p>
	 */
	protected void initHousekeeper() {

		Housekeeper housekeeper = new Housekeeper(this);
		MSingleton.baseLookup(this,MHousekeeper.class).register(housekeeper, getConfig().getLong("housekeeper_sleep",30000), true);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Look into the pool for an unused DbProvider. If no one find, create one.
	 */
	@Override
	public DbConnection getConnection() throws Exception {
		log().t(getName(),"getConnection");
		boolean foundClosed = false;
		try {
			synchronized (pool) {
				for (InternalDbConnection con : pool) {
					if (con.isClosed() || con.checkTimedOut()) {
						foundClosed = true;
					} else
						if (!con.isUsed()) {
							con.setUsed(true);
							return new DbConnectionProxy(this, con);
						}
				}
				try {
					InternalDbConnection con = getProvider().createConnection();
					if (con == null) return null;
					con.setPool(this);
					pool.add(con);
					con.setUsed(true);
					//getDialect().initializeConnection(con, this);
					return new DbConnectionProxy(this, con);
				} catch (Exception e) {
					// special behavior for e.g. mysql, retry to get a connection after gc()
					// Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException: Too many connections
					if (e.getMessage().indexOf("Too many connections") > -1) {
						printStackTrace();
					}
					throw e;
				}
			}
		} finally {
			if (foundClosed) cleanup(false);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Current pool size.
	 */
	@Override
	@JmxManaged(descrition="Current size of the pool")
	public int getSize() {
		synchronized (pool) {
			return pool.size();
		}
	}

	/** {@inheritDoc} */
	@Override
	@JmxManaged(descrition="Current used connections in the pool")
	public int getUsedSize() {
		int cnt = 0;
		synchronized (pool) {
			for (DbConnection con : new LinkedList<DbConnection>(pool)) {
				if( con.isUsed()) cnt++;
			}
		}
		return cnt;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Cleanup the connection pool. Unused or closed connections will be removed.
	 * TODO new strategy to remove unused connections - not prompt, need a timeout time or minimum pool size.
	 */
	@Override
	@JmxManaged(descrition="Cleanup unused connections")
	public void cleanup(boolean unusedAlso) {
		log().t(getName(),"cleanup");
		synchronized (pool) {
			for (InternalDbConnection con : new LinkedList<InternalDbConnection>(pool)) {
				try {
					con.checkTimedOut();
					if( unusedAlso && !con.isUsed() || con.isClosed()) {
						con.close();
						pool.remove(con);
					}
				} catch (Throwable t) {} // for secure - do not impact the thread
			}
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * Close the pool and all connections.
	 */
	@Override
	public void close() {
		if (pool == null) return;
		log().t(getName(),"close");
		synchronized (pool) {
			for (DbConnection con : pool) {
				con.close();
			}
			pool = null;
		}
	}

	/** {@inheritDoc} */
	@Override
	@JmxManaged(descrition="Return the usage of the connections")
	public String dumpUsage(boolean used) {
		StringBuffer out = new StringBuffer();
		synchronized (pool) {
			for (ConnectionTrace trace : getStackTraces().values()) {
				out.append(trace.toString()).append("\n");
			}
		}
		return out.toString();
	}

	private static class Housekeeper extends MHousekeeperTask {

		private WeakReference<DefaultDbPool> pool;
		private String name;

		private Housekeeper(DefaultDbPool pool) {
			this.pool = new WeakReference<DefaultDbPool>(pool);
			this.name = pool.getPoolId();
		}

		@Override
		public void doit() throws Exception {
			DefaultDbPool obj = pool.get();
			if (obj == null || obj.isClosed()) {
				log().t(name,"close");
				cancel();
				return;
			}
			log().t(getClass(),name,"Housekeeping");
			obj.cleanup(false);
		}

	}

	/** {@inheritDoc} */
	@Override
	public boolean isClosed() {
		return pool == null;
	}
}
