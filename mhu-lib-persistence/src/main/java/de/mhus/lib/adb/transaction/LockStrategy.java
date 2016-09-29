package de.mhus.lib.adb.transaction;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.MLog;

public abstract class LockStrategy extends MLog {

	public abstract void lock(Persistable object, String key, Transaction transaction,
			long timeout);
	
	public abstract void releaseLock(Persistable object, String key,
			Transaction transaction);
	
}
