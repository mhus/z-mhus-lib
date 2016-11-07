package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.errors.NotSupportedException;

public class TimeoutMap<K,V> implements Map<K,V> {

	private Map<K,Container<V>> map = new HashMap<>();
	private long timeout = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 10;
	private long lastCheck = System.currentTimeMillis();
	private long checkTimeout = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 10;
	private Invalidator<K,V> invalidator;

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public V get(Object key) {
		doValidationCheck();
		TimeoutMap<K, V>.Container<V> ret = map.get(key);
		return ret == null ? null : ret.getValue();
	}

	public long getAccessCount(Object key) {
		doValidationCheck();
		TimeoutMap<K, V>.Container<V> ret = map.get(key);
		return ret == null ? -1 : ret.accessed;
	}
	
	public synchronized void doValidationCheck() {
		if (System.currentTimeMillis() - lastCheck > checkTimeout) {
			Iterator<java.util.Map.Entry<K, TimeoutMap<K, V>.Container<V>>> entries = map.entrySet().iterator();
			while (entries.hasNext()) {
				 java.util.Map.Entry<K, TimeoutMap<K, V>.Container<V>> next = entries.next();
				 if (invalidator != null && invalidator.isInvalid(next.getKey(), next.getValue().value, next.getValue().time, next.getValue().accessed )
					 ||
					 invalidator == null && next.getValue().isTimeout())
					 entries.remove();
			}
			lastCheck = System.currentTimeMillis();
		}
	}

	@Override
	public V put(K key, V value) {
		doValidationCheck();
		TimeoutMap<K, V>.Container<V> ret = map.put(key, new Container<V>(value));
		return ret == null ? null : ret.value;
	}

	@Override
	public V remove(Object key) {
		doValidationCheck();
		TimeoutMap<K, V>.Container<V> ret = map.remove(key);
		return ret == null ? null : ret.value;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (java.util.Map.Entry<? extends K, ? extends V> entry : m.entrySet())
			put(entry.getKey(), entry.getValue());
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public Set<K> keySet() {
		doValidationCheck();
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		throw new NotSupportedException();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		doValidationCheck();
		HashSet<java.util.Map.Entry<K, V>> out = new HashSet<>();
		for (Map.Entry<K, Container<V>>entry : map.entrySet())
			out.add(new MapEntry<K,V>( entry.getKey(), entry.getValue().value) );
		return out;
	}

	@Override
	public boolean equals(Object o) {
		return map.equals(o);
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	@Override
	public V getOrDefault(Object key, V defaultValue) {
		V ret = get(key);
		if (ret == null) return defaultValue;
		return ret;
	}

	@Override
	public void forEach(BiConsumer<? super K, ? super V> action) {
		throw new NotSupportedException();
	}

	@Override
	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
		throw new NotSupportedException();
	}

	@Override
	public V putIfAbsent(K key, V value) {
		throw new NotSupportedException();
	}

	@Override
	public boolean remove(Object key, Object value) {
		throw new NotSupportedException();
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		throw new NotSupportedException();
	}

	@Override
	public V replace(K key, V value) {
		throw new NotSupportedException();
	}

	@Override
	public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
		throw new NotSupportedException();
	}

	@Override
	public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		throw new NotSupportedException();
	}

	@Override
	public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		throw new NotSupportedException();
	}

	@Override
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		throw new NotSupportedException();
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public long getCheckTimeout() {
		return checkTimeout;
	}

	public void setCheckTimeout(long checkTimeout) {
		this.checkTimeout = checkTimeout;
	}

	public Invalidator<K,V> getInvalidator() {
		return invalidator;
	}

	public void setInvalidator(Invalidator<K,V> invalidator) {
		this.invalidator = invalidator;
	}

	private class Container<Z> {
		Z value;
		long time = System.currentTimeMillis();
		long accessed = 0;

		public Container(Z value) {
			this.value = value;
		}

		public Z getValue() {
			time = System.currentTimeMillis();
			accessed++;
			return value;
		}

		boolean isTimeout() {
			return System.currentTimeMillis() - time > getTimeout();
		}
	}
	
	public static interface Invalidator<K,V> {

		boolean isInvalid(K key, V value, long time, long accessed);
		
	}
}
