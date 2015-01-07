package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class SetCast<F,T> implements Set<T> {

	private Set<F> from;

	public SetCast(Set<F> from) {
		this.from = from;
	}
	
	@Override
	public int size() {
		return from.size();
	}

	@Override
	public boolean isEmpty() {
		return from.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return from.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return new IteratorCast<F, T>(from.iterator());
	}

	@Override
	public Object[] toArray() {
		return from.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return from.toArray(a);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean add(T e) {
		return from.add((F)e);
	}

	@Override
	public boolean remove(Object o) {
		return from.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return from.containsAll(c);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return from.addAll((Collection<? extends F>) c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return from.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return from.removeAll(c);
	}

	@Override
	public void clear() {
		from.clear();
	}

}
