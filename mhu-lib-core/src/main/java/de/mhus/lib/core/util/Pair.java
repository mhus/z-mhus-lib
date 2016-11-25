package de.mhus.lib.core.util;

public class Pair<K,V> {

	private K key;
	private V value;
	
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.valueOf(key);
	}
	
	@Override
	public boolean equals(Object in) {
		if (in == null) return false;
		if (in instanceof Pair<?, ?>)
			return ((Pair<?, ?>)in).getKey().equals(key);
		return key.equals(in);
	}
}
