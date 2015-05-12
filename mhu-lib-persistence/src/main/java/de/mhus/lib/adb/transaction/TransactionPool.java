package de.mhus.lib.adb.transaction;

public class TransactionPool {

	private static TransactionPool instance;
	private ThreadLocal<Transaction> pool = new ThreadLocal<>();

	public static synchronized TransactionPool instance() {
		if (instance == null)
			instance = new TransactionPool();
		return instance;
	}
	
	public Transaction get() {
		synchronized (pool) {
			return pool.get();
		}
	}
	
	public void release() {
		synchronized (pool) {
			Transaction out = pool.get();
			pool.remove();
			out.release();
		}
	}
	
	public void lock(long timeout, Transaction transaction) {
		synchronized (pool) {
			pool.set(transaction);
			transaction.lock(timeout);
		}
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
