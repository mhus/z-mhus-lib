package de.mhus.lib.sql;

import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.core.lang.MObject;

public abstract class InternalDbConnection extends MObject implements DbConnection {

	protected DbPool pool;
	protected String poolId;
	protected long creationTime = 0;
	protected long lastUsedTime = 0;
	protected long timeoutUnused    = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 10;
	protected long timeoutLifetime = MTimeInterval.HOUR_IN_MILLISECOUNDS;
	
	public InternalDbConnection() {
		creationTime = System.currentTimeMillis();
	}
	
	public void setPool(DbPool pool) {
		this.poolId = pool.getPoolId();
		this.pool = pool;
	}

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

	public void setUsed(boolean used) {
		lastUsedTime = System.currentTimeMillis();
	}

}
