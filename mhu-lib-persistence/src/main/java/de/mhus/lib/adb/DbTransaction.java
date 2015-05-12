package de.mhus.lib.adb;

import de.mhus.lib.adb.transaction.TransactionEncapsulation;
import de.mhus.lib.adb.transaction.TransactionLock;
import de.mhus.lib.adb.transaction.TransactionPool;
import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.errors.TimeoutRuntimeException;

/**
 * Allow transaction and lock manageent with adb framework. This implementation should be used if you not need to
 * synchronize transactions with other resources. A JTA implementation is not planed but can be implemented
 * on top of this implementation. The transaction is based on the current thread. If you leave the thread you will also
 * leave the current transaction.
 * 
 * @author mikehummel
 *
 */
public class DbTransaction {

	public static final long DEFAULT_TIMEOUT = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 10;

	public static void lock(Persistable ... objects) throws TimeoutRuntimeException {
		lock(DEFAULT_TIMEOUT, objects);
	}
	
	public static void lock(long timeout, Persistable ... objects) throws TimeoutRuntimeException {
		TransactionPool.instance().lock(timeout, new TransactionLock(objects));
	}
	
	public static void lock(DbManager manager, Persistable ... objects) throws TimeoutRuntimeException {
		lock(manager, DEFAULT_TIMEOUT, objects);
	}
	
	public static void lock(DbManager manager, long timeout, Persistable ... objects) throws TimeoutRuntimeException {
		TransactionPool.instance().lock(timeout, new TransactionLock(manager, objects));
	}
	
	public static void release() {
		TransactionPool.instance().release();
	}
	
//	public static void begin() {
//		TransactionPool.instance().set(new TransactionEncapsulation() );	
//	}
//
//	public static void commit() {
//		TransactionPool.instance().commit();
//	}
//
//	public static void rollback() {
//		TransactionPool.instance().rollback();
//	}
	
}
