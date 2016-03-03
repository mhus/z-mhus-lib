package de.mhus.lib.adb.transaction;

import java.util.LinkedList;
import java.util.Set;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.TimeoutRuntimeException;

/**
 * <p>Abstract Transaction class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public abstract class Transaction extends MLog {

	private LinkedList<Transaction> nested;

	/**
	 * <p>Constructor for Transaction.</p>
	 */
	public Transaction() {
	}
	
	/**
	 * <p>lock.</p>
	 *
	 * @param timeout a long.
	 * @throws de.mhus.lib.errors.TimeoutRuntimeException if any.
	 */
	public abstract void lock(long timeout) throws TimeoutRuntimeException;

	/**
	 * <p>release.</p>
	 */
	public abstract void release();

	/**
	 * <p>getDbManager.</p>
	 *
	 * @return a {@link de.mhus.lib.adb.DbManager} object.
	 */
	public abstract DbManager getDbManager();

	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return toString();
	}

	/**
	 * <p>pushNestedLock.</p>
	 *
	 * @param transaction a {@link de.mhus.lib.adb.transaction.Transaction} object.
	 */
	public synchronized void pushNestedLock(Transaction transaction) {
		if (nested == null) nested = new LinkedList<>();
		nested.add(transaction);
	}

	/**
	 * <p>popNestedLock.</p>
	 *
	 * @return a {@link de.mhus.lib.adb.transaction.Transaction} object.
	 */
	public Transaction popNestedLock() {
		if (nested == null || nested.size() == 0) return null;
		return nested.removeLast();
	}

	/**
	 * <p>Getter for the field <code>nested</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.adb.transaction.Transaction} object.
	 */
	public Transaction getNested() {
		if (nested == null || nested.size() == 0) return null;
		return nested.getLast();
	}

	/**
	 * <p>getLockKeys.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public abstract Set<String> getLockKeys();
	
}
