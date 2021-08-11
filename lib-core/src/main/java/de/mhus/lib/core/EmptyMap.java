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
package de.mhus.lib.core;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class EmptyMap<K, V> implements Map<K, V> {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public void putAll(Map m) {}

    @Override
    public void clear() {}

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keySet() {
        return (Set<K>) MCollection.EMPTY_SET;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<V> values() {
        return (Collection<V>) MCollection.EMPTY_LIST;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Entry<K, V>> entrySet() {
        return (Set<Entry<K, V>>) MCollection.EMPTY_SET;
    }
}
