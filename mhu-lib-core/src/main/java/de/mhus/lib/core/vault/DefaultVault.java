package de.mhus.lib.core.vault;

import java.util.HashMap;
import java.util.UUID;

import de.mhus.lib.core.lang.MObject;

public class DefaultVault extends MObject implements MVault {
	
	private HashMap<String, VaultSource> sources = new HashMap<>();
	
	@Override
	public void registerSource(VaultSource source) {
		if (source == null) return;
		synchronized (sources) {
			sources.put(source.getName(), source);
		}
	}

	@Override
	public void unregisterSource(String sourceName) {
		if (sourceName == null) return;
		synchronized (sources) {
			sources.remove(sourceName);
		}
	}

	@Override
	public String[] getSourceNames() {
		synchronized (sources) {
			return sources.keySet().toArray(new String[sources.size()]);
		}
	}

	@Override
	public VaultSource getSource(String name) {
		synchronized (sources) {
			return sources.get(name);
		}
	}

	@Override
	public VaultEntry getEntry(UUID id) {
		synchronized (sources) {
			for (VaultSource source : sources.values()) {
				VaultEntry res = source.getEntry(id);
				if (res != null) return res;
			}
		}
		return null;
	}

}
