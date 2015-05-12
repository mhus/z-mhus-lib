package de.mhus.lib.adb.transaction;

import java.util.UUID;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.TimeoutRuntimeException;

public abstract class Transaction extends MLog {

	public Transaction() {
	}
	
	public abstract void lock(long timeout) throws TimeoutRuntimeException;

	public abstract void release();

	public abstract DbManager getDbManager();

	public String getName() {
		return toString();
	}

}
