package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import de.mhus.lib.basics.ReadOnly;

public class EmptySet<T> implements Set<T>, ReadOnly {

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return new EmptyIterator<T>();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> E[] toArray(E[] a) {
		return null;
	}

	@Override
	public boolean add(T e) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {
	}

}
