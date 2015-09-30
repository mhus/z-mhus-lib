package de.mhus.lib.adb.transaction;

public class TransactionPool {

	private static TransactionPool instance;
	private ThreadLocal<Transaction> pool = new ThreadLocal<>();

	public static synchronized TransactionPool instance() {
		if (instance == null)
			instance = new TransactionPool();
		return instance;
	}
	
	/**
	 * Return current transaction, if cascaded transactions, return the last/current
	 * @return
	 */
	public Transaction get() {
//		synchronized (pool) {
			Transaction out = pool.get();
			Transaction nested = out.getNested();
			if (nested != null) return nested;
			return out;
//		}
	}
	
	/**
	 * Return current transaction, if cascaded transactions, return the base
	 * @return
	 */
	public Transaction getBase() {
//		synchronized (pool) {
			Transaction out = pool.get();
			return out;
//		}
	}
	
	public void release() {
//		synchronized (pool) {
			Transaction out = pool.get();
			if (out == null) return;
			Transaction nested = out.popNestedLock();
			if (nested == null) {
				pool.remove();
				out.release();
			}
//		}
	}
	
	public void lock(long timeout, Transaction transaction) {
//		synchronized (pool) {
			Transaction current = pool.get();
			if (current != null)
				current.pushNestedLock(transaction);
			else {
				pool.set(transaction);
				transaction.lock(timeout);
			}
//		}
	}

//	public void commit() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public void rollback() {
//		// TODO Auto-generated method stub
//		
//	}
	
	
}
