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

import java.util.Set;

import de.mhus.lib.adb.DbManager;
import de.mhus.lib.errors.NotSupportedException;
import de.mhus.lib.errors.TimeoutRuntimeException;

public class TransactionEncapsulation extends Transaction {

	@Override
	public void release() {
		throw new NotSupportedException();
	}

	@Override
	public void lock(long timeout) throws TimeoutRuntimeException {
		throw new NotSupportedException();
	}

	@Override
	public DbManager getDbManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getLockKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
