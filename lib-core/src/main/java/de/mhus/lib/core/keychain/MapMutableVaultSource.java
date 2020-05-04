/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.keychain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.MException;

public abstract class MapMutableVaultSource extends MLog implements MutableVaultSource {

    protected HashMap<UUID, KeyEntry> entries = new HashMap<>();
    protected String name = UUID.randomUUID().toString();

    @Override
    public KeyEntry getEntry(UUID id) {
        doCheckSource();
        synchronized (entries) {
            return entries.get(id);
        }
    }

    @Override
    public KeyEntry getEntry(String name) {
        doCheckSource();
        synchronized (entries) {
            // TODO optimize !!!
            for (KeyEntry entry : entries.values())
                if (name.equals(entry.getName())) return entry;
        }
        return null;
    }

    @Override
    public Set<UUID> getEntryIds() {
        doCheckSource();
        synchronized (entries) {
            return Collections.unmodifiableSet(entries.keySet());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addEntry(KeyEntry entry) throws MException {
        doCheckSource();
        synchronized (entries) {
            entries.put(entry.getId(), new DefaultEntry(entry));
        }
    }

    @Override
    public void updateEntry(KeyEntry entry) throws MException {
        doCheckSource();
        synchronized (entries) {
            entries.put(entry.getId(), new DefaultEntry(entry));
        }
    }

    @Override
    public void removeEntry(UUID id) throws MException {
        doCheckSource();
        synchronized (entries) {
            entries.remove(id);
        }
    }

    protected abstract void doCheckSource();

    @Override
    public String toString() {
        return MSystem.toString(this, name, entries.size());
    }
}
