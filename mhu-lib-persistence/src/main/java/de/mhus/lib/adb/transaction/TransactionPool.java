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
	 * @return x
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
	 * @return x
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
