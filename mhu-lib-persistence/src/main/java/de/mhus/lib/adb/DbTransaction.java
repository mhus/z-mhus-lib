/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.adb;

import de.mhus.lib.adb.transaction.TransactionLock;
import de.mhus.lib.adb.transaction.TransactionPool;
import de.mhus.lib.annotations.adb.DbTransactionable;
import de.mhus.lib.annotations.adb.TransactionConnection;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.TimeoutRuntimeException;

/**
 * Allow transaction and lock manageent with adb framework. This implementation should be used if you not need to
 * synchronize transactions with other resources. A JTA implementation is not planed but can be implemented
 * on top of this implementation. The transaction is based on the current thread. If you leave the thread you will also
 * leave the current transaction.
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class DbTransaction {

	/** Constant <code>DEFAULT_TIMEOUT=MTimeInterval.MINUTE_IN_MILLISECOUNDS * 10</code> */
	public static final long DEFAULT_TIMEOUT = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 10;

	/**
	 * <p>lock.</p>
	 *
	 * @param objects a {@link de.mhus.lib.adb.Persistable} object.
	 * @throws de.mhus.lib.errors.TimeoutRuntimeException if any.
	 */
	public static void lock(Persistable ... objects) throws TimeoutRuntimeException {
		lock(DEFAULT_TIMEOUT, objects);
	}
	
	/**
	 * <p>lock.</p>
	 *
	 * @param timeout a long.
	 * @param objects a {@link de.mhus.lib.adb.Persistable} object.
	 * @throws de.mhus.lib.errors.TimeoutRuntimeException if any.
	 */
	public static void lock(long timeout, Persistable ... objects) throws TimeoutRuntimeException {
		TransactionPool.instance().lock(timeout, new TransactionLock(objects));
	}
	
	/**
	 * <p>lock.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param objects a {@link de.mhus.lib.adb.Persistable} object.
	 * @throws de.mhus.lib.errors.TimeoutRuntimeException if any.
	 */
	public static void lock(DbManager manager, Persistable ... objects) throws TimeoutRuntimeException {
		lock(manager, DEFAULT_TIMEOUT, objects);
	}
	
	/**
	 * <p>lock.</p>
	 *
	 * @param manager a {@link de.mhus.lib.adb.DbManager} object.
	 * @param timeout a long.
	 * @param objects a {@link de.mhus.lib.adb.Persistable} object.
	 * @throws de.mhus.lib.errors.TimeoutRuntimeException if any.
	 */
	public static void lock(DbManager manager, long timeout, Persistable ... objects) throws TimeoutRuntimeException {
		TransactionPool.instance().lock(timeout, new TransactionLock(manager, objects));
	}
	
	/**
	 * <p>release.</p>
	 */
	public static void releaseLock() {
		TransactionPool.instance().releaseLock();
	}
	
	public static void releaseEncapsulate() {
		TransactionPool.instance().releaseEncapsulate();
	}
	
	public static void encapsulate(DbTransactionable owner) {
		TransactionPool.instance().encapsulate(owner);
	}
	
	public static TransactionConnection getConnection(DbTransactionable owner) {
		return TransactionPool.instance().getConnection(owner);
	}
	
	public static boolean commitAndRelease() {
		return TransactionPool.instance().commitAndRelease();
	}
	
	public static boolean rollbackAndRelease() throws MException {
		return TransactionPool.instance().rollbackAndRelease();
	}
	
	public static boolean commitWithoutRelease() {
		return TransactionPool.instance().commit();
	}
	
	public static boolean rollbackWithoutRelease() throws MException {
		return TransactionPool.instance().rollback();
	}
	
}
