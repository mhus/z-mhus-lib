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

import java.util.HashMap;

import de.mhus.lib.annotations.adb.DbTransactionable;
import de.mhus.lib.annotations.adb.TransactionConnection;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.logging.MLogUtil;

public class Encapsulation {
	
	private HashMap<String, TransactionConnection> connections = new HashMap<>();
	
	public void append(DbTransactionable owner) {
		String id = MSystem.getObjectId(owner);
		synchronized (connections) {
			if (connections.containsKey(id)) return; // already set
			TransactionConnection con = owner.createTransactionalConnection();
			if (con != null)
				connections.put(id, con);
		}
	}

	public boolean commit() {
		boolean success = true;
		synchronized (connections) {
			for (TransactionConnection con : connections.values())
				try {
					con.commit();
				} catch (Throwable e) {
					success = false;
					MLogUtil.log().w(e);
				}
		}
		return success;
	}

	public boolean rollback() {
		boolean success = true;
		synchronized (connections) {
			for (TransactionConnection con : connections.values())
				try {
					con.rollback();
				} catch (Throwable e) {
					success = false;
					MLogUtil.log().w(e);
				}
		}
		return success;
	}
	
	public void clear() {
		synchronized (connections) {
			connections.clear();
		}
	}
	
	public TransactionConnection getCurrent(DbTransactionable owner) {
		String id = MSystem.getObjectId(owner);
		synchronized (connections) {
			return connections.get(id);
		}
	}
	
	public boolean isEmpty() {
		return connections.isEmpty();
	}

}
