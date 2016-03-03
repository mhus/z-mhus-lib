package de.mhus.lib.core.util;

import java.util.HashMap;

/**
 * <p>VectorMap class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class VectorMap<K1,K2,V> extends HashMap<K1, HashMap<K2, V>> {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>get.</p>
	 *
	 * @param k1 a K1 object.
	 * @param k2 a K2 object.
	 * @return a V object.
	 */
	public V get(K1 k1, K2 k2) {
		HashMap<K2, V> map = get(k1, false);
		if (map == null) return null;
		return map.get(k2);
	}

	/**
	 * <p>get.</p>
	 *
	 * @param k1 a K1 object.
	 * @param create a boolean.
	 * @return a {@link java.util.HashMap} object.
	 */
	public HashMap<K2, V> get(K1 k1, boolean create) {
		synchronized (this) {
			HashMap<K2, V> out = get(k1);
			if (out == null && create) {
				out = new HashMap<>();
				put(k1, out);
			}
			return out;
		}
	}
	
	/**
	 * <p>removeValue.</p>
	 *
	 * @param k1 a K1 object.
	 * @param k2 a K2 object.
	 * @return a V object.
	 */
	public V removeValue(K1 k1, K2 k2) {
		synchronized (this) {
			HashMap<K2, V> map = get(k1, false);
			if (map == null) return null;
			V out = map.remove(k2);
			if (map.size() == 0) {
				remove(k1); // cleanup
			}
			return out;
		}
	}
	
	/**
	 * <p>containsKey.</p>
	 *
	 * @param k1 a K1 object.
	 * @param k2 a K2 object.
	 * @return a boolean.
	 */
	public boolean containsKey(K1 k1, K2 k2) {
		HashMap<K2, V> map = get(k1, false);
		if (map == null) return false;
		return map.containsKey(k2);
	}
	
	/**
	 * <p>put.</p>
	 *
	 * @param k1 a K1 object.
	 * @param k2 a K2 object.
	 * @param v a V object.
	 * @return a V object.
	 */
	public V put(K1 k1, K2 k2, V v) {
		synchronized (this) {
			HashMap<K2, V> map = get(k1, true);
			return map.put(k2, v);
		}
	}
	
}
