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

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.mhus.lib.core.MTimeInterval;
import de.mhus.lib.errors.NotSupportedException;

public class SoftTimeoutMap<K,V> implements Map<K,V> {

	private Map<K,Container<V>> map = new HashMap<>();
	private long timeout = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 10;
	private long lastCheck = System.currentTimeMillis();
	private long checkTimeout = MTimeInterval.MINUTE_IN_MILLISECOUNDS * 10;
	private Invalidator<K,V> invalidator;
	private boolean refreshOnAccess = true;

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
		SoftTimeoutMap<K, V>.Container<V> ret = map.get(key);
		if (ret == null) return null;
		if (ret.isTimeout()) {
			remove(ret);
			return null;
		}
		return ret.getValue();
	}

	public long getAccessCount(Object key) {
		doValidationCheck();
		SoftTimeoutMap<K, V>.Container<V> ret = map.get(key);
		return ret == null ? -1 : ret.accessed;
	}
	
	public synchronized void doValidationCheck() {
		if (MTimeInterval.isTimeOut(lastCheck, checkTimeout)) {
			map.entrySet().removeIf(e -> {
				 return (invalidator != null && invalidator.isInvalid(e.getKey(), e.getValue().get(), e.getValue().time, e.getValue().accessed )
						 ||
						 e.getValue().isTimeout());
			});
			lastCheck = System.currentTimeMillis();
		}
	}

	@Override
	public V put(K key, V value) {
		doValidationCheck();
		SoftTimeoutMap<K, V>.Container<V> ret = map.put(key, new Container<V>(value));
		return ret == null ? null : ret.get();
	}

	@Override
	public V remove(Object key) {
		doValidationCheck();
		SoftTimeoutMap<K, V>.Container<V> ret = map.remove(key);
		return ret == null ? null : ret.get();
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
			out.add(new MapEntry<K,V>( entry.getKey(), entry.getValue().get()) );
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

	public boolean isRefreshOnAccess() {
		return refreshOnAccess;
	}

	public void setRefreshOnAccess(boolean refreshOnAccess) {
		this.refreshOnAccess = refreshOnAccess;
	}

	private class Container<Z> extends SoftReference<Z> {
		long time = System.currentTimeMillis();
		long accessed = 0;

		public Container(Z value) {
			super(value);
		}

		public Z getValue() {
			if (isRefreshOnAccess())
				time = System.currentTimeMillis();
			accessed++;
			return super.get();
		}

		boolean isTimeout() {
			return MTimeInterval.isTimeOut(time, timeout) || super.get() == null;
		}
	}
	
	public static interface Invalidator<K,V> {

		boolean isInvalid(K key, V value, long time, long accessed);
		
	}
}
