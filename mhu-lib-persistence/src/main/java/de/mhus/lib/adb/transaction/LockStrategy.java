package de.mhus.lib.adb.transaction;

import de.mhus.lib.adb.Persistable;
import de.mhus.lib.core.MLog;

/**
 * <p>Abstract LockStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public abstract class LockStrategy extends MLog {

	/**
	 * <p>lock.</p>
	 *
	 * @param object a {@link de.mhus.lib.adb.Persistable} object.
	 * @param key a {@link java.lang.String} object.
	 * @param transaction a {@link de.mhus.lib.adb.transaction.Transaction} object.
	 * @param timeout a long.
	 */
	public abstract void lock(Persistable object, String key, Transaction transaction,
			long timeout);
	
	/**
	 * <p>releaseLock.</p>
	 *
	 * @param object a {@link de.mhus.lib.adb.Persistable} object.
	 * @param key a {@link java.lang.String} object.
	 * @param transaction a {@link de.mhus.lib.adb.transaction.Transaction} object.
	 */
	public abstract void releaseLock(Persistable object, String key,
			Transaction transaction);
	
}
