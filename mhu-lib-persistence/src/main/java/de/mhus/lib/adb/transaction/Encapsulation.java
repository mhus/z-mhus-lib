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

import java.util.LinkedList;

import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.sql.DbConnection;

public class Encapsulation {
	
	private LinkedList<DbConnection> queue = new LinkedList<>();
	
	public void append(DbConnection con) {
		queue.add(con);
	}

	public DbConnection getCurrent() {
		if (queue.isEmpty()) return null;
		return queue.getLast();
	}

	public void shift() {
		if (queue.isEmpty()) return;
		DbConnection last = queue.removeLast();
		// for secure the default behavior
		try {
			if (last != null)
				last.commit();
		} catch (Throwable e) {
			MLogUtil.log().w(e);
		}
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

}
