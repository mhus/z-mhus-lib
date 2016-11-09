package de.mhus.lib.core.cfg;

import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;

import de.mhus.lib.core.MSingleton;

public class UpdaterCfg {

	@SuppressWarnings("rawtypes")
	private WeakHashMap<CfgValue, String> registry = new WeakHashMap<>();
	
	@SuppressWarnings("rawtypes")
	public void register(CfgValue configValue) {
		synchronized (registry) {
			registry.put(configValue,"");
		}
	}

	public void doUpdate(String owner) {
		LinkedList<CfgValue> list = null;
		synchronized (registry) {
			list = new LinkedList<CfgValue>(registry.keySet());
		}
		for (CfgValue<?> item : list)
		if (owner == null || item.isOwner(owner)) // is not working at all, owner could be a super class
			item.update();
	}

	public List<CfgValue> getList() {
		synchronized (registry) {
			return new LinkedList<CfgValue>(registry.keySet());
		}
	}
	
}
