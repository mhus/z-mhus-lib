package de.mhus.lib.core.jmx;

import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

public class JmxWeakMap extends JmxObject implements JmxWeakMapMBean {

	private WeakHashMap<?, ?> map;

	public JmxWeakMap(Object owner, String name, WeakHashMap<?, ?> map) {
		if (owner != null) setJmxPackage(owner.getClass().getCanonicalName());
		setJmxName(name);
		this.map = map;
	}

	@Override
	public int getSize() {
		return map.size();
	}

	@Override
	public String[] getEntries() {
		LinkedList<String> out = new LinkedList<String>();
		for (Map.Entry<?, ?> item : map.entrySet()) {
			out.add(item.getKey() + "=" + item.getValue());
		}
		return out.toArray(new String[out.size()]);
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public void remove(String key) {
		map.remove(key);
	}
	
}
