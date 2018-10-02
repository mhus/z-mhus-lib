package de.mhus.lib.core.vault;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSystem;
import de.mhus.lib.errors.MException;

public abstract class MapMutableVaultSource extends MLog implements MutableVaultSource {

	protected HashMap<UUID, VaultEntry> entries = new HashMap<>();
	protected String name = UUID.randomUUID().toString();
	
	@Override
	public VaultEntry getEntry(UUID id) {
		synchronized (entries) {
			return entries.get(id);
		}
	}

	@Override
	public Set<UUID> getEntryIds() {
		synchronized (entries) {
			return Collections.unmodifiableSet(entries.keySet());
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void addEntry(VaultEntry entry) throws MException {
		synchronized (entries) {
			entries.put(entry.getId(), entry);
		}
	}
	
	@Override
	public void removeEntry(UUID id) throws MException {
		synchronized (entries) {
			entries.remove(id);
		}
	}
		
	@Override
	public String toString() {
		return MSystem.toString(this, name, entries.size());
	}

}
