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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.mhus.lib.errors.NotSupportedException;

public class ImmutableMap<K, V> implements Map<K, V> {

    private HashMap<K, V> map;

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return map.toString();
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
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public V put(K key, V value) {
        // return map.put(key, value);
        throw new NotSupportedException();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        //		map.putAll(m);
        throw new NotSupportedException();
    }

    @Override
    public V remove(Object key) {
        //		return map.remove(key);
        throw new NotSupportedException();
    }

    @Override
    public void clear() {
        //		map.clear();
        throw new NotSupportedException();
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public V putIfAbsent(K key, V value) {
        //		return map.putIfAbsent(key, value);
        throw new NotSupportedException();
    }

    @Override
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        //		return map.replace(key, oldValue, newValue);
        throw new NotSupportedException();
    }

    @Override
    public V replace(K key, V value) {
        //		return map.replace(key, value);
        throw new NotSupportedException();
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(
            K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return map.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return map.compute(key, remappingFunction);
    }

    @Override
    public V merge(
            K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return map.merge(key, value, remappingFunction);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        //		map.forEach(action);
        throw new NotSupportedException();
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        //		map.replaceAll(function);
        throw new NotSupportedException();
    }

    @Override
    public Object clone() {
        return map.clone();
    }

    public static <K, V> MapBuilder<K, V> builder() {
        return new MapBuilder<K, V>();
    }

    public static class MapBuilder<K, V> {

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
