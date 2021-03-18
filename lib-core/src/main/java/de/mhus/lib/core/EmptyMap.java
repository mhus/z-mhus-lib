package de.mhus.lib.core;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class EmptyMap<K,V> implements Map<K,V> {

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
    public void putAll(Map m) {
    }

    @Override
    public void clear() {
    }

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
