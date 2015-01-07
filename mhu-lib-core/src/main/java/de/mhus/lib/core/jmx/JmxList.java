package de.mhus.lib.core.jmx;

import java.util.LinkedList;
import java.util.List;

public class JmxList extends JmxObject implements JmxListMBean {

	private List<?> list;

	public JmxList(Object owner, String name, List<?> list) {
		if (owner != null) setJmxPackage(owner.getClass().getCanonicalName());
		setJmxName(name);
		this.list = list;
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public String[] getEntries() {
		LinkedList<String> out = new LinkedList<String>();
		for (Object item : list) {
			out.add(String.valueOf(item));
		}
		return out.toArray(new String[out.size()]);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public void remove(int key) {
		list.remove(key);
	}
	
}
