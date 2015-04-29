package de.mhus.lib.core.util;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SoftHashMap<K,V> implements Map<K,V>, Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<K, SoftReference<V>> map = new HashMap<>();
	private long lastCleanup;

	
	public SoftHashMap() {
		
	}
		
	private SoftHashMap(HashMap<K, SoftReference<V>> map) {
		this.map = map;
	}
	
	private void periodicCleanup() {
		if (System.currentTimeMillis() - lastCleanup < 1000 * 60) return;
		cleanup();
	}
	
	public void cleanup() {
		synchronized (map) {
			Iterator<Entry<K, SoftReference<V>>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				java.util.Map.Entry<K, SoftReference<V>> entry = iter.next();
				if (entry.getValue() == null || entry.getValue().get() == null)
					iter.remove();
			}
			lastCleanup = System.currentTimeMillis();
		}
	}
	
	@Override
	public boolean equals(Object o) {
		synchronized (map) {
			return map.equals(o);
		}
	}

	@Override
	public int size() {
		periodicCleanup();
		synchronized (map) {
			return map.size();
		}
	}

	@Override
	public boolean isEmpty() {
		periodicCleanup();
		synchronized (map) {
			return map.isEmpty();
		}
	}

	@Override
	public V get(Object key) {
		periodicCleanup();
		synchronized (map) {
			SoftReference<V> s = map.get(key);
			V v = s.get();
			if (v == null) {
				map.remove(key);
				return null;
			}
			return v;
		}
	}

	@Override
	public int hashCode() {
		synchronized (map) {
			return map.hashCode();
		}
	}

	@Override
	public boolean containsKey(Object key) {
		periodicCleanup();
		synchronized (map) {
			return map.containsKey(key);
		}
	}

	@Override
	public String toString() {
		synchronized (map) {
			return map.toString();
		}
	}

	@Override
	public V put(K key, V value) {
		periodicCleanup();
		synchronized (map) {
			SoftReference<V> s = map.put(key, new SoftReference<V>(value));
			if (s == null) return null;
			return s.get();
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (java.util.Map.Entry<? extends K, ? extends V> entry : m.entrySet())
			put(entry.getKey(), entry.getValue());
	}

	@Override
	public V remove(Object key) {
		periodicCleanup();
		synchronized (map) {
			SoftReference<V> s = map.remove(key);
			if (s == null) return null;
			return s.get();
		}
	}

	@Override
	public void clear() {
		synchronized (map) {
			map.clear();
		}
	}

	@Override
	public boolean containsValue(Object value) {
		periodicCleanup();
		synchronized (map) {
			return map.containsValue(value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object clone() {
		cleanup();
		synchronized (map) {
			return new SoftHashMap((HashMap) map.clone());
		}
	}

	@Override
	public Set<K> keySet() {
		periodicCleanup();
		synchronized (map) {
			return map.keySet();
		}
	}

	@Override
	public Collection<V> values() {
		periodicCleanup();
		synchronized (map) {
			return new CollectionWrap(map.values());
		}
	}

	@Override
	public Set<java.util.Map.Entry<K,V>> entrySet() {
		periodicCleanup();
		synchronized (map) {
			return new EntrySetWrap(map.entrySet());
		}
	}

	private class EntrySetWrap implements Set<java.util.Map.Entry<K,V>> {

		private Set<java.util.Map.Entry<K, SoftReference<V>>> set;

		@Override
		public int size() {
			return set.size();
		}

		@Override
		public boolean isEmpty() {
			return set.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return set.contains(o);
		}

		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new MapIteratorWrap(set.iterator());
		}

		@Override
		public Object[] toArray() {
			return set.toArray();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return set.toArray(a);
		}

		@Override
		public boolean add(java.util.Map.Entry<K, V> e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(Object o) {
			return set.remove(o);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return set.containsAll(c);
		}

		@Override
		public boolean addAll(
				Collection<? extends java.util.Map.Entry<K, V>> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return set.retainAll(c);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return set.removeAll(c);
		}

		@Override
		public void clear() {
			set.clear();
		}

		@Override
		public boolean equals(Object o) {
			return set.equals(o);
		}

		@Override
		public int hashCode() {
			return set.hashCode();
		}

		public EntrySetWrap( Set<java.util.Map.Entry<K, SoftReference<V>>> entrySet) {
			this.set = entrySet;
		}

	}

	private class MapIteratorWrap implements Iterator<java.util.Map.Entry<K, V>> {

		private Iterator<java.util.Map.Entry<K, SoftReference<V>>> itertor;

		@Override
		public boolean hasNext() {
			return itertor.hasNext();
		}

		@Override
		public java.util.Map.Entry<K, V> next() {
			return new MapEntryWrap(itertor.next());
		}

		@Override
		public void remove() {
			itertor.remove();
		}

		public MapIteratorWrap(Iterator<java.util.Map.Entry<K, SoftReference<V>>> iterator) {
			this.itertor = iterator;
		}

	}

	private class MapEntryWrap implements java.util.Map.Entry<K, V> {

		private java.util.Map.Entry<K, SoftReference<V>> next;

		@Override
		public K getKey() {
			return next.getKey();
		}

		@Override
		public V getValue() {
			return next.getValue().get();
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean equals(Object o) {
			return next.equals(o);
		}

		@Override
		public int hashCode() {
			return next.hashCode();
		}

		public MapEntryWrap(java.util.Map.Entry<K, SoftReference<V>> next) {
			this.next = next;
		}

	}

	private class CollectionWrap implements Collection<V> {

		private Collection<SoftReference<V>> col;

		@Override
		public int size() {
			return col.size();
		}

		@Override
		public boolean isEmpty() {
			return col.isEmpty();
		}

		@Override
		public boolean contains(Object o) {
			return col.contains(o);
		}

		@Override
		public Iterator<V> iterator() {
			return new IteratorWrap(col.iterator());
		}

		@Override
		public Object[] toArray() {
			return col.toArray();
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return col.toArray(a);
		}

		@Override
		public boolean add(V e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean remove(Object o) {
			return col.remove(o);
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return col.containsAll(c);
		}

		@Override
		public boolean addAll(Collection<? extends V> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return col.removeAll(c);
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return col.retainAll(c);
		}

		@Override
		public void clear() {
			col.clear();
		}

		@Override
		public boolean equals(Object o) {
			return col.equals(o);
		}

		@Override
		public int hashCode() {
			return col.hashCode();
		}

		public CollectionWrap(Collection<SoftReference<V>> col) {
			this.col = col;
		}

	}

	private class IteratorWrap implements Iterator<V> {

		private Iterator<SoftReference<V>> iterator;

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public V next() {
			return iterator.next().get();
		}

		@Override
		public void remove() {
			iterator.remove();
		}

		public IteratorWrap(Iterator<SoftReference<V>> iterator) {
			this.iterator = iterator;
		}

	}
}
