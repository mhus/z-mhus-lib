package de.mhus.lib.sql;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.directory.ResourceNode;

public class PseudoDbPool extends DbPool {

	private boolean closed;

	public PseudoDbPool() throws Exception {
		super(null,null);
	}

	/**
	 * Create a new pool from a configuration.
	 * 
	 * @param config Config element or null. null will use the central MSingleton configuration.
	 * @param activator Activator or null. null will use the central MSingleton Activator.
	 * @throws Exception
	 */
	public PseudoDbPool(ResourceNode config,MActivator activator) throws Exception {
		super(config,activator);
	}

	/**
	 * Create a pool with the DbProvider.
	 * 
	 * @param provider
	 */
	public PseudoDbPool(DbProvider provider) {
		super(provider);
	}

	@Override
	public DbConnection getConnection() throws Exception {
		if (closed) throw new Exception("Pool is closed");
		InternalDbConnection out = getProvider().createConnection();
		out.setPool(this);
		out.setUsed(true);
		return out;
	}

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public int getUsedSize() {
		return 0;
	}

	@Override
	public void cleanup(boolean unusedAlso) {

	}

	@Override
	public void close() {
		closed = true;
	}

	@Override
	public String dumpUsage(boolean used) {
		return "?";
	}

	@Override
	public boolean isClosed() {
		return closed;
	}

}
