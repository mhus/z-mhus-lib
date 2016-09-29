package de.mhus.lib.core.cfg;

import java.util.LinkedList;
import java.util.WeakHashMap;

/**
 * <p>UpdaterCfg class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class UpdaterCfg {

	@SuppressWarnings("rawtypes")
	private WeakHashMap<CfgValue, String> registry = new WeakHashMap<>();
	
	/**
	 * <p>register.</p>
	 *
	 * @param configValue a {@link de.mhus.lib.core.cfg.CfgValue} object.
	 */
	@SuppressWarnings("rawtypes")
	public void register(CfgValue configValue) {
		synchronized (registry) {
			registry.put(configValue,"");
		}
	}

	/**
	 * <p>doUpdate.</p>
	 *
	 * @param owner a {@link java.lang.String} object.
	 */
	public void doUpdate(String owner) {
		LinkedList<CfgValue> list = null;
		synchronized (registry) {
			list = new LinkedList<CfgValue>(registry.keySet());
		}
		for (CfgValue<?> item : list)
		if (owner == null || item.isOwner(owner)) // is not working at all, owner could be a super class
			item.update();
	}
	
}
