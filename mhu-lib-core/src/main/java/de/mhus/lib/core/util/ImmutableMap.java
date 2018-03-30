/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.mhus.lib.errors.NotSupportedException;

public class ImmutableMap<K, V> implements Map<K,V> {

	private HashMap<K, V> map;

	public boolean equals(Object o) {
		return map.equals(o);
	}

	public int hashCode() {
		return map.hashCode();
	}

	public String toString() {
		return map.toString();
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public V get(Object key) {
		return map.get(key);
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public V put(K key, V value) {
		//return map.put(key, value);
		throw new NotSupportedException();
	}

	public void putAll(Map<? extends K, ? extends V> m) {
//		map.putAll(m);
		throw new NotSupportedException();
	}

	public V remove(Object key) {
//		return map.remove(key);
		throw new NotSupportedException();
	}

	public void clear() {
//		map.clear();
		throw new NotSupportedException();
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Collection<V> values() {
		return map.values();
	}

	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	public V getOrDefault(Object key, V defaultValue) {
		return map.getOrDefault(key, defaultValue);
	}

	public V putIfAbsent(K key, V value) {
//		return map.putIfAbsent(key, value);
		throw new NotSupportedException();
	}

	public boolean remove(Object key, Object value) {
		return map.remove(key, value);
	}

	public boolean replace(K key, V oldValue, V newValue) {
//		return map.replace(key, oldValue, newValue);
		throw new NotSupportedException();
	}

	public V replace(K key, V value) {
//		return map.replace(key, value);
		throw new NotSupportedException();
	}

	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		return map.computeIfAbsent(key, mappingFunction);
	}

	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return map.computeIfPresent(key, remappingFunction);
	}

	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return map.compute(key, remappingFunction);
	}

	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return map.merge(key, value, remappingFunction);
	}

	public void forEach(BiConsumer<? super K, ? super V> action) {
//		map.forEach(action);
		throw new NotSupportedException();
	}

	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
//		map.replaceAll(function);
		throw new NotSupportedException();
	}

	public Object clone() {
		return map.clone();
	}

	public static <K, V> MapBuilder<K, V> builder() {
		return new MapBuilder<K, V>();
	}

	public static class MapBuilder<K,V> {

		HashMap<K, V> map = new HashMap<>();
		
		public MapBuilder<K, V> put(K k, V v) {
			map.put(k, v);
			return this;
		}

		public Map<K, V> build() {
			ImmutableMap<K, V> out = new ImmutableMap<>();
			out.map = map;
			return out;
		}
		
	}
}
