/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.mhus.lib.core.MPeriod;
import de.mhus.lib.errors.NotSupportedException;

public class SoftTimeoutMap<K, V> implements Map<K, V> {

    private Map<K, Container<V>> map = Collections.synchronizedMap(new HashMap<>());
    private long timeout = MPeriod.MINUTE_IN_MILLISECONDS * 10;
    private long lastCheck = System.currentTimeMillis();
    private long checkTimeout = MPeriod.MINUTE_IN_MILLISECONDS * 10;
    private Invalidator<K, V> invalidator;
    private boolean refreshOnAccess = true;

    public SoftTimeoutMap() {}

    public SoftTimeoutMap(long timeout) {
        this.timeout = timeout;
        this.checkTimeout = timeout;
    }

    public SoftTimeoutMap(long timeout, boolean refreshOnAccess) {
        this.timeout = timeout;
        this.checkTimeout = timeout;
        this.refreshOnAccess = refreshOnAccess;
    }

    public SoftTimeoutMap(long timeout, long checkTimeout, boolean refreshOnAccess) {
        this.timeout = timeout;
        this.checkTimeout = checkTimeout;
        this.refreshOnAccess = refreshOnAccess;
    }

    public SoftTimeoutMap(
            long timeout,
            long checkTimeout,
            boolean refreshOnAccess,
            Invalidator<K, V> invalidator) {
        this.timeout = timeout;
        this.checkTimeout = checkTimeout;
        this.refreshOnAccess = refreshOnAccess;
        this.invalidator = invalidator;
    }

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
        if (MPeriod.isTimeOut(lastCheck, checkTimeout)) {
            map.entrySet()
                    .removeIf(
                            e -> {
                                return (invalidator != null
                                                && invalidator.isInvalid(
                                                        e.getKey(),
                                                        e.getValue().get(),
                                                        e.getValue().time,
                                                        e.getValue().accessed)
                                        || e.getValue().isTimeout());
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
        for (Map.Entry<K, Container<V>> entry : map.entrySet())
            out.add(new MapEntry<K, V>(entry.getKey(), entry.getValue().get()));
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
    public V computeIfPresent(
            K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new NotSupportedException();
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new NotSupportedException();
    }

    @Override
    public V merge(
            K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new NotSupportedException();
    }

    public long getTimeout() {
        return timeout;
    }

    /**
     * Set the maximal life time for every entry. After the timeout the entry will be removed.
     *
     * @param timeout timeout in ms
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getCheckTimeout() {
        return checkTimeout;
    }

    /**
     * Set the time after a full check should be done. If the map is accesses after timeout a full
     * check over all elements will be performed. Set to -1 to disable.
     *
     * @param checkTimeout timeout in ms
     */
    public void setCheckTimeout(long checkTimeout) {
        this.checkTimeout = checkTimeout;
    }

    public Invalidator<K, V> getInvalidator() {
        return invalidator;
    }

    /**
     * Set a function which can decide if the entry is no more valid.
     *
     * @param invalidator
     */
    public void setInvalidator(Invalidator<K, V> invalidator) {
        this.invalidator = invalidator;
    }

    public boolean isRefreshOnAccess() {
        return refreshOnAccess;
    }

    /**
     * Set to true if also get requests will reset the timeout. If set to false it will timeout even
     * it was read. Default is true.
     *
     * @param refreshOnAccess
     */
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
            if (isRefreshOnAccess()) time = System.currentTimeMillis();
            accessed++;
            return super.get();
        }

        boolean isTimeout() {
            return MPeriod.isTimeOut(time, timeout) || super.get() == null;
        }
    }

    public static interface Invalidator<K, V> {

        boolean isInvalid(K key, V value, long time, long accessed);
    }
}
