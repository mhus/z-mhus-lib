package de.mhus.lib.core.jmx;

import java.util.LinkedList;
import java.util.Map;

/**
 * <p>JmxMap class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JmxMap extends JmxObject implements JmxMapMBean {

	private Map<?, ?> map;

	/**
	 * <p>Constructor for JmxMap.</p>
	 *
	 * @param owner a {@link java.lang.Object} object.
	 * @param name a {@link java.lang.String} object.
	 * @param map a {@link java.util.Map} object.
	 */
	public JmxMap(Object owner, String name, Map<?, ?> map) {
		if (owner != null) setJmxPackage(owner.getClass().getCanonicalName());
		setJmxName(name);
		this.map = map;
	}

	/** {@inheritDoc} */
	@Override
	public int getSize() {
		return map.size();
	}

	/** {@inheritDoc} */
	@Override
	public String[] getEntries() {
		LinkedList<String> out = new LinkedList<String>();
		for (Map.Entry<?, ?> item : map.entrySet()) {
			out.add(item.getKey() + "=" + item.getValue());
		}
		return out.toArray(new String[out.size()]);
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		map.clear();
	}

	/** {@inheritDoc} */
	@Override
	public void remove(String key) {
		map.remove(key);
	}
	
}
