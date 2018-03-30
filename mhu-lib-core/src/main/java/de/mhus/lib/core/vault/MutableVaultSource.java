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
package de.mhus.lib.core.vault;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.NotSupportedException;

public abstract class MutableVaultSource extends MLog implements VaultSource {
	
	protected HashMap<UUID, VaultEntry> entries = new HashMap<>();
	protected String name = UUID.randomUUID().toString();
	
	@Override
	public VaultEntry getEntry(UUID id) {
		synchronized (entries) {
			return entries.get(id);
		}
	}

	@Override
	public UUID[] getEntryIds() {
		synchronized (entries) {
			return entries.keySet().toArray(new UUID[entries.size()]);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T adaptTo(Class<? extends T> ifc) throws NotSupportedException {
		if (ifc.isInstance(this)) return (T) this;
		throw new NotSupportedException(this,ifc);
	}

	@Override
	public String getName() {
		return name;
	}

	public void addEntry(VaultEntry entry) throws MException {
		synchronized (entries) {
			entries.put(entry.getId(), entry);
		}
	}
	
	public void removeEntry(UUID id) throws MException {
		synchronized (entries) {
			entries.remove(id);
		}
	}

	public abstract void doLoad() throws IOException;
	
	public abstract void doSave() throws IOException;
	
	@Override
	public String toString() {
		return MSystem.toString(this, name, entries.size());
	}

}
