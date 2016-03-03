package de.mhus.lib.core.configupdater;

import java.util.LinkedList;
import java.util.WeakHashMap;

/**
 * <p>ConfigUpdater class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class ConfigUpdater {

	@SuppressWarnings("rawtypes")
	private WeakHashMap<ConfigValue, String> registry = new WeakHashMap<>();
	
	/**
	 * <p>register.</p>
	 *
	 * @param configValue a {@link de.mhus.lib.core.configupdater.ConfigValue} object.
	 */
	@SuppressWarnings("rawtypes")
	public void register(ConfigValue configValue) {
		synchronized (registry) {
			registry.put(configValue,"");
		}
	}

	/**
	 * <p>doUpdate.</p>
	 */
	public void doUpdate() {
		LinkedList<ConfigValue> list = null;
		synchronized (registry) {
			list = new LinkedList<ConfigValue>(registry.keySet());
		}
		for (ConfigValue item : list)
			item.update();
	}
	
}
