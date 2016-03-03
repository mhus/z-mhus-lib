package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>FallbackMap class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class FallbackMap<K, V> implements Map<K, V> {

	private Map<K, V> map;
	private Map<K, V> fall;
	private boolean fro = true;
	
	/**
	 * <p>Constructor for FallbackMap.</p>
	 *
	 * @param map a {@link java.util.Map} object.
	 * @param fallback a {@link java.util.Map} object.
	 * @param fallbackReadOnly a boolean.
	 */
	public FallbackMap(Map<K, V> map, Map<K, V> fallback, boolean fallbackReadOnly) {
		this.map = map;
		fall = fallback;
		fro = fallbackReadOnly;
	}
	
	/** {@inheritDoc} */
	@Override
	public int size() {
		return Math.max(map.size(), fall.size());
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return map.isEmpty() && fall.isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key) || fall.containsKey(key);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value) || fall.containsValue(value);
	}

	/** {@inheritDoc} */
	@Override
	public V get(Object key) {
		V ret = map.get(key);
		if (ret == null) return fall.get(key);
		return ret;
	}

	/** {@inheritDoc} */
	@Override
	public V put(K key, V value) {
		V ret = get(key);
		map.put(key, value);
		return ret;
	}

	/** {@inheritDoc} */
	@Override
	public V remove(Object key) {
		V ret = get(key);
		map.remove(key);
		if (!fro) fall.remove(key);
		return ret;
	}

	/** {@inheritDoc} */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		map.clear();
		if (!fro) fall.clear();
	}

	/** {@inheritDoc} */
	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	/** {@inheritDoc} */
	@Override
	public Collection<V> values() {
		return map.values();
	}

	/** {@inheritDoc} */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

}
