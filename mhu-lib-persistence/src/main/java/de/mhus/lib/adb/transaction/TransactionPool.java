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

import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.MException;
import de.mhus.lib.sql.DbConnection;

public class TransactionPool extends MLog {

	private static TransactionPool instance;
	private ThreadLocal<LockBase> lock = new ThreadLocal<>();
	private ThreadLocal<Encapsulation> encapsulate = new ThreadLocal<>();

	public static synchronized TransactionPool instance() {
		if (instance == null)
			instance = new TransactionPool();
		return instance;
	}
	
	/**
	 * Return current transaction, if cascaded transactions, return the last/current
	 * @return x
	 */
	public LockBase getLock() {
//		synchronized (pool) {
			LockBase out = lock.get();
			LockBase nested = out.getNested();
			if (nested != null) return nested;
			return out;
//		}
	}
	
	/**
	 * Return current transaction, if cascaded transactions, return the base
	 * @return x
	 */
	public LockBase getLockBase() {
//		synchronized (pool) {
			LockBase out = lock.get();
			return out;
//		}
	}
	
	public void releaseLock() {
//		synchronized (pool) {
			LockBase out = lock.get();
			if (out == null) return;
			LockBase nested = out.popNestedLock();
			if (nested == null) {
				lock.remove();
				out.release();
			}
//		}
	}
	
	public void lock(long timeout, LockBase transaction) {
//		synchronized (pool) {
			LockBase current = lock.get();
			if (current != null)
				current.pushNestedLock(transaction);
			else {
				lock.set(transaction);
				transaction.lock(timeout);
			}
//		}
	}

	public void encapsulate(DbConnection con) {
//		synchronized (encapsulate) {
			Encapsulation enc = encapsulate.get();
			if (enc == null) {
				enc = new Encapsulation();
				encapsulate.set(enc);
			}
			enc.append(con);
//		}
	}
	
	public void releaseEncapsulate() {
		Encapsulation enc = encapsulate.get();
		if (enc == null)
			return;
		enc.shift();
		if (enc.isEmpty())
			encapsulate.remove();
	}
	
	public DbConnection getConnection() {
		Encapsulation enc = encapsulate.get();
		if (enc == null)
			return null;
		if (enc.isEmpty())
			return null;
		return enc.getCurrent();
	}
			
	public void commit() {
//		synchronized (encapsulate) {
			Encapsulation enc = encapsulate.get();
			if (enc == null) {
				log().d("encapsulate not enabled - ignore commit");
				return;
			}
			DbConnection cur = enc.getCurrent();
			if (cur == null) {
				log().d("encapsulation has no connection - ignore commit");
				return;
			}
			try {
				cur.commit();
			} catch (Exception e) {
				log().e(e);
			}
//		}
	}
	
	public void rollback() throws MException {
//		synchronized (encapsulate) {
			Encapsulation enc = encapsulate.get();
			if (enc == null)
				throw new MException("encapsulate not enabled for rollback");
			DbConnection cur = enc.getCurrent();
			if (cur == null)
				throw new MException("encapsulate no connection for rollback");
			try {
				cur.rollback();
			} catch (Exception e) {
				log().e(e);
			}
//		}
	}
	
	
}
