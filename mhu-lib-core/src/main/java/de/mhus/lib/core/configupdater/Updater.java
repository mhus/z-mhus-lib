package de.mhus.lib.core.configupdater;

import java.util.LinkedList;
import java.util.WeakHashMap;

public class Updater {

	@SuppressWarnings("rawtypes")
	private WeakHashMap<ConfigValue, String> registry = new WeakHashMap<>();
	
	@SuppressWarnings("rawtypes")
	public void register(ConfigValue configValue) {
		synchronized (registry) {
			registry.put(configValue,"");
		}
	}

	public void doUpdate(String owner) {
		LinkedList<ConfigValue> list = null;
		synchronized (registry) {
			list = new LinkedList<ConfigValue>(registry.keySet());
		}
		for (ConfigValue<?> item : list)
		if (owner == null || item.isOwner(owner)) // is not working at all, owner could be a super class
			item.update();
	}
	
}
