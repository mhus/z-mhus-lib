package de.mhus.lib.core.configupdater;

import java.util.LinkedList;
import java.util.WeakHashMap;

public class ConfigUpdater {

	@SuppressWarnings("rawtypes")
	private WeakHashMap<ConfigValue, String> registry = new WeakHashMap<>();
	
	@SuppressWarnings("rawtypes")
	public void register(ConfigValue configValue) {
		synchronized (registry) {
			registry.put(configValue,"");
		}
	}

	public void doUpdate() {
		LinkedList<ConfigValue> list = null;
		synchronized (registry) {
			list = new LinkedList<ConfigValue>(registry.keySet());
		}
		for (ConfigValue item : list)
			item.update();
	}
	
}
