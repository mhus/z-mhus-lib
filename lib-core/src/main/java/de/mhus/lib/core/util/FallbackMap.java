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
import java.util.Map;
import java.util.Set;

public class FallbackMap<K, V> implements Map<K, V> {

    private Map<K, V> map;
    private Map<K, V> fall;
    private boolean fro = true;

    public FallbackMap(Map<K, V> map, Map<K, V> fallback, boolean fallbackReadOnly) {
        this.map = map;
        fall = fallback;
        fro = fallbackReadOnly;
    }

    @Override
    public int size() {
        return Math.max(map.size(), fall.size());
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty() && fall.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key) || fall.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value) || fall.containsValue(value);
    }

    @Override
    public V get(Object key) {
        V ret = map.get(key);
        if (ret == null) return fall.get(key);
        return ret;
    }

    @Override
    public V put(K key, V value) {
        V ret = get(key);
        map.put(key, value);
        return ret;
    }

    @Override
    public V remove(Object key) {
        V ret = get(key);
        map.remove(key);
        if (!fro) fall.remove(key);
        return ret;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
        if (!fro) fall.clear();
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
}
