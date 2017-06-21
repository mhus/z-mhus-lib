package de.mhus.lib.core.system;

import java.util.WeakHashMap;

import de.mhus.lib.annotations.jmx.JmxManaged;
import de.mhus.lib.core.jmx.MJmx;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.logging.MLogFactory;

@JmxManaged
public class SingleMLogInstanceFactory extends MJmx implements MLogFactory {

	private WeakHashMap<String, Log> cache = new WeakHashMap<>();
	
	@SuppressWarnings("rawtypes")
	@Override
	public Log lookup(Object owner) {
		String name = null;
		if (owner == null)
			name = null;
		else
		if (owner instanceof String)
			name = (String)owner;
		else
		if (owner instanceof Class)
			name = ((Class)owner).getCanonicalName();
		else
			name = owner.getClass().getCanonicalName();
		synchronized (this) {
			Log log = cache.get(name);
			if (log == null)
				log = new Log(name);
			cache.put(name,log);
			return log;
		}
	}
	
	@JmxManaged
	public int getCacheSize() {
		return cache.size();
	}

	@Override
	public void update() {
		synchronized (this) {
			for (Log l : cache.values())
				l.update();
		}
	}

}
