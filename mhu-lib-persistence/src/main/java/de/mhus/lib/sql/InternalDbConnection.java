package de.mhus.lib.sql;

import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.lang.MObject;

/**
 * <p>Abstract InternalDbConnection class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class InternalDbConnection extends MObject implements DbConnection {

	protected DbPool pool;
	protected String poolId;
	protected long creationTime = 0;
	protected long lastUsedTime = 0;
	protected long timeoutUnused    = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 10;
	protected long timeoutLifetime = MTimeInterval.HOUR_IN_MILLISECOUNDS;

	/**
	 * <p>Constructor for InternalDbConnection.</p>
	 */
	public InternalDbConnection() {
		creationTime = System.currentTimeMillis();
	}

	/**
	 * <p>Setter for the field <code>pool</code>.</p>
	 *
	 * @param pool a {@link de.mhus.lib.sql.DbPool} object.
	 */
	public void setPool(DbPool pool) {
		this.poolId = pool.getPoolId();
		this.pool = pool;
	}

	/**
	 * <p>checkTimedOut.</p>
	 *
	 * @return a boolean.
	 */
	public boolean checkTimedOut() {
		if (isUsed()) return false;
		long currentTime = System.currentTimeMillis();
		if ( (currentTime - creationTime > timeoutLifetime) || (lastUsedTime != 0 && currentTime - lastUsedTime > timeoutUnused) ) {
			log().t("timeout");
			close();
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void setUsed(boolean used) {
		lastUsedTime = System.currentTimeMillis();
	}

}
