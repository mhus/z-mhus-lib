package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.lang.NullValue;

public class NullableMap<K,V> implements Map<K, V> {
	
	private static final NullValue NULL_VALUE = new NullValue();
	private HashMap<K, Object> impl = new HashMap<K, Object>();

	public int size() {
		return impl.size();
	}

	public boolean isEmpty() {
		return impl.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public V get(Object key) {
		Object ret = impl.get(key);
		if (ret instanceof NullValue)
			return null;
		return (V) ret;
	}

	public boolean equals(Object o) {
		return impl.equals(o);
	}

	public boolean containsKey(Object key) {
		return impl.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	public V put(K key, V value) {
		if (value == null) {
			Object ret = impl.put(key, NULL_VALUE);
			if (ret instanceof NullValue)
				return null;
			return (V) ret;
		}
			
		Object ret = impl.put(key, value);
		if (ret instanceof NullValue)
			return null;
		return (V) ret;
	}

	public int hashCode() {
		return impl.hashCode();
	}

	public String toString() {
		return impl.toString();
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		impl.putAll(m);
	}

	@SuppressWarnings("unchecked")
	public V remove(Object key) {
		Object ret = impl.remove(key);
		if (ret instanceof NullValue)
			return null;
		return (V) ret;
	}

	public void clear() {
		impl.clear();
	}

	public boolean containsValue(Object value) {
		return impl.containsValue(value);
	}

	public Object clone() {
		return impl.clone();
	}

	public Set<K> keySet() {
		return impl.keySet();
	}

	@SuppressWarnings("unchecked")
	public Collection<V> values() {
		LinkedList<V> out = new LinkedList<V>();
		for (Object o : impl.values()) {
			if (! (o instanceof NullValue))
					out.add((V) o);
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public Set<Map.Entry<K, V>> entrySet() {
		LinkedList<Map.Entry<K,V>> out = new LinkedList<Map.Entry<K,V>>();
		for (Map.Entry<K, Object> o : impl.entrySet()) {
			if (! (o instanceof NullValue))
					out.add(new MyEntry(o) );
		}
		return (Set<java.util.Map.Entry<K, V>>) out;
	}
	
	
	private class MyEntry implements Map.Entry<K, V> {

		private K k;
		private Object v;
		
		public MyEntry(java.util.Map.Entry<K, Object> o) {
			this.k = o.getKey();
			this.v = o.getValue();
		}

		@Override
		public K getKey() {
			return k;
		}

		@SuppressWarnings("unchecked")
		@Override
		public V getValue() {
			if (v instanceof NullValue)
				return null;
			return (V) v;
		}

		@Override
		public V setValue(V value) {
			return null;
		}
		
	}
}
