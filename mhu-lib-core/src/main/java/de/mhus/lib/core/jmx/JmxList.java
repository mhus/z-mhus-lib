package de.mhus.lib.core.jmx;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>JmxList class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JmxList extends JmxObject implements JmxListMBean {

	private List<?> list;

	/**
	 * <p>Constructor for JmxList.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param name a {@link java.lang.String} object.
	 * @param list a {@link java.util.List} object.
	 */
	public JmxList(Object owner, String name, List<?> list) {
		if (owner != null) setJmxPackage(owner.getClass().getCanonicalName());
		setJmxName(name);
		this.list = list;
	}

	/** {@inheritDoc} */
	@Override
	public int getSize() {
		return list.size();
	}

	/** {@inheritDoc} */
	@Override
	public String[] getEntries() {
		LinkedList<String> out = new LinkedList<String>();
		for (Object item : list) {
			out.add(String.valueOf(item));
		}
		return out.toArray(new String[out.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		list.clear();
	}

	/** {@inheritDoc} */
	@Override
	public void remove(int key) {
		list.remove(key);
	}
	
}
