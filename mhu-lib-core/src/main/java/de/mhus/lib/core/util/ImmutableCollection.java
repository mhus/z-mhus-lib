package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Iterator;

public class ImmutableCollection<E> implements Collection<E> {

	private Collection<E> parent;

	public ImmutableCollection(Collection<E> parent) {
		this.parent = parent;
	}

	@Override
	public boolean add(E o) {
		return false;
	}

	public void add(int index, E element) {
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return false;
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean contains(Object o) {
		return parent.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return parent.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return parent.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return new ImmutableIterator<E>(parent.iterator());
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	public E remove(int index) {
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	public E set(int index, E element) {
		return null;
	}

	@Override
	public int size() {
		return parent.size();
	}

	@Override
	public Object[] toArray() {
		return parent.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return parent.toArray(a);
	}

}
