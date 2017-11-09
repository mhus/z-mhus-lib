package de.mhus.lib.core.vault;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.MSystem;
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

	public void addEntry(VaultEntry entry) {
		synchronized (entries) {
			entries.put(entry.getId(), entry);
		}
	}
	
	public void removeEntry(UUID id) {
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
