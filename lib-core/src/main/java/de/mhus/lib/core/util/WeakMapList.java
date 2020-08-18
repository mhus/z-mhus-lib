package de.mhus.lib.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WeakMapList<K,V> extends HashMap<K, List<V>>{

    private static final long serialVersionUID = 1L;

    public void putEntry(K k, V v) {
        List<V> list = get(k);
        if (list == null) {
            list = new WeakList<>();
            put(k, list);
        }
        list.add(v);
    }
    
    public void removeEntry(K k, V v) {
        List<V> list = get(k);
        if (list == null) return;
        list.remove(v);
        if (list.size() == 0)
            remove(k);
    }
    
    public V removeEntry(K k, int index) {
        List<V> list = get(k);
        if (list == null) return null;
        V ret = list.remove(index);
        if (list.size() == 0)
            remove(k);
        return ret;
    }
    
    public V getFirst(K k) {
        List<V> list = get(k);
        if (list == null || list.size() == 0) return null;
        return list.get(0);
    }

    public V getLast(K k) {
        List<V> list = get(k);
        if (list == null || list.size() == 0) return null;
        return list.get(list.size()-1);
    }

    public List<V> getClone(K key) {
        synchronized(this) {
            List<V> list = get(key);
            if (list == null) return Collections.emptyList();
            ((WeakList<V>)list).cleanupWeak();
            return new ArrayList<>(list);
        }
    }

}
