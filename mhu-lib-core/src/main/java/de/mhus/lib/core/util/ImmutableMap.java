package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.mhus.lib.errors.NotSupportedException;

/**
 * <p>ImmutableMap class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class ImmutableMap<K, V> implements Map<K,V> {

	private HashMap<K, V> map;

	/** {@inheritDoc} */
	public boolean equals(Object o) {
		return map.equals(o);
	}

	/**
	 * <p>hashCode.</p>
	 *
	 * @return a int.
	 */
	public int hashCode() {
		return map.hashCode();
	}

	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString() {
		return map.toString();
	}

	/**
	 * <p>size.</p>
	 *
	 * @return a int.
	 */
	public int size() {
		return map.size();
	}

	/**
	 * <p>isEmpty.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/** {@inheritDoc} */
	public V get(Object key) {
		return map.get(key);
	}

	/** {@inheritDoc} */
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	/**
	 * <p>put.</p>
	 *
	 * @param key a K object.
	 * @param value a V object.
	 * @return a V object.
	 */
	public V put(K key, V value) {
		//return map.put(key, value);
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	public void putAll(Map<? extends K, ? extends V> m) {
//		map.putAll(m);
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	public V remove(Object key) {
//		return map.remove(key);
		throw new NotSupportedException();
	}

	/**
	 * <p>clear.</p>
	 */
	public void clear() {
//		map.clear();
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	/**
	 * <p>keySet.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public Set<K> keySet() {
		return map.keySet();
	}

	/**
	 * <p>values.</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public Collection<V> values() {
		return map.values();
	}

	/**
	 * <p>entrySet.</p>
	 *
	 * @return a {@link java.util.Set} object.
	 */
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	/**
	 * <p>getOrDefault.</p>
	 *
	 * @param key a {@link java.lang.Object} object.
	 * @param defaultValue a V object.
	 * @return a V object.
	 */
	public V getOrDefault(Object key, V defaultValue) {
		return map.getOrDefault(key, defaultValue);
	}

	/**
	 * <p>putIfAbsent.</p>
	 *
	 * @param key a K object.
	 * @param value a V object.
	 * @return a V object.
	 */
	public V putIfAbsent(K key, V value) {
//		return map.putIfAbsent(key, value);
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	public boolean remove(Object key, Object value) {
		return map.remove(key, value);
	}

	/**
	 * <p>replace.</p>
	 *
	 * @param key a K object.
	 * @param oldValue a V object.
	 * @param newValue a V object.
	 * @return a boolean.
	 */
	public boolean replace(K key, V oldValue, V newValue) {
//		return map.replace(key, oldValue, newValue);
		throw new NotSupportedException();
	}

	/**
	 * <p>replace.</p>
	 *
	 * @param key a K object.
	 * @param value a V object.
	 * @return a V object.
	 */
	public V replace(K key, V value) {
//		return map.replace(key, value);
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		return map.computeIfAbsent(key, mappingFunction);
	}

	/** {@inheritDoc} */
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return map.computeIfPresent(key, remappingFunction);
	}

	/** {@inheritDoc} */
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return map.compute(key, remappingFunction);
	}

	/** {@inheritDoc} */
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return map.merge(key, value, remappingFunction);
	}

	/** {@inheritDoc} */
	public void forEach(BiConsumer<? super K, ? super V> action) {
//		map.forEach(action);
		throw new NotSupportedException();
	}

	/** {@inheritDoc} */
	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
//		map.replaceAll(function);
		throw new NotSupportedException();
	}

	/**
	 * <p>clone.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object clone() {
		return map.clone();
	}

	/**
	 * <p>builder.</p>
	 *
	 * @param <K> a K object.
	 * @param <V> a V object.
	 * @return a {@link de.mhus.lib.core.util.ImmutableMap.MapBuilder} object.
	 */
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
