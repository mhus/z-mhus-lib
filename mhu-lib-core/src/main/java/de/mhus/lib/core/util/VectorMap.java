package de.mhus.lib.core.util;

import java.util.HashMap;

public class VectorMap<K1,K2,V> extends HashMap<K1, HashMap<K2, V>> {

	private static final long serialVersionUID = 1L;

	public V get(K1 k1, K2 k2) {
		HashMap<K2, V> map = get(k1, false);
		if (map == null) return null;
		return map.get(k2);
	}

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
	
	public boolean containsKey(K1 k1, K2 k2) {
		HashMap<K2, V> map = get(k1, false);
		if (map == null) return false;
		return map.containsKey(k2);
	}
	
	public V put(K1 k1, K2 k2, V v) {
		synchronized (this) {
			HashMap<K2, V> map = get(k1, true);
			return map.put(k2, v);
		}
	}
	
}
