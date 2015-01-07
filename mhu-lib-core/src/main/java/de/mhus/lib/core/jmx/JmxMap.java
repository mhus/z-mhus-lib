package de.mhus.lib.core.jmx;

import java.util.LinkedList;
import java.util.Map;

public class JmxMap extends JmxObject implements JmxMapMBean {

	private Map<?, ?> map;

	public JmxMap(Object owner, String name, Map<?, ?> map) {
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
