package de.mhus.lib.core.util;

import java.util.Map;

import de.mhus.lib.basics.ReadOnly;

public class MapEntry<K, V> implements Map.Entry<K, V>, ReadOnly {

	private K key;
	private V value;

	public MapEntry(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		return null;
	}

}
