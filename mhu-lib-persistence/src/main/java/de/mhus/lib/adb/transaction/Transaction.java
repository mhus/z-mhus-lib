package de.mhus.lib.adb.transaction;

import java.util.LinkedList;
import java.util.Set;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.TimeoutRuntimeException;

public abstract class Transaction extends MLog {

	private LinkedList<Transaction> nested;

	public Transaction() {
	}
	
	public abstract void lock(long timeout) throws TimeoutRuntimeException;

	public abstract void release();

	public abstract DbManager getDbManager();

	public String getName() {
		return toString();
	}

	public synchronized void pushNestedLock(Transaction transaction) {
		if (nested == null) nested = new LinkedList<>();
		nested.add(transaction);
	}

	public Transaction popNestedLock() {
		if (nested == null || nested.size() == 0) return null;
		return nested.removeLast();
	}

	public Transaction getNested() {
		if (nested == null || nested.size() == 0) return null;
		return nested.getLast();
	}

	public abstract Set<String> getLockKeys();
	
}
