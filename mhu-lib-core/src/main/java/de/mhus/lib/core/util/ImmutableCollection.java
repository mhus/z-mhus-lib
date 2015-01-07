package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Iterator;

public class ImmutableCollection<E> implements Collection<E> {

	private Collection<E> parent;

	public ImmutableCollection(Collection<E> parent) {
		this.parent = parent;
	}

	public boolean add(E o) {
		return false;
	}

	public void add(int index, E element) {
	}

	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return false;
	}

	public void clear() {
	}

	public boolean contains(Object o) {
		return parent.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return parent.containsAll(c);
	}

	public boolean isEmpty() {
		return parent.isEmpty();
	}

	public Iterator<E> iterator() {
		return new ImmutableIterator<E>(parent.iterator());
	}

	public boolean remove(Object o) {
		return false;
	}

	public E remove(int index) {
		return null;
	}

	public boolean removeAll(Collection<?> c) {
		return false;
	}

	public boolean retainAll(Collection<?> c) {
		return false;
	}

	public E set(int index, E element) {
		return null;
	}

	public int size() {
		return parent.size();
	}

	public Object[] toArray() {
		return parent.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return parent.toArray(a);
	}

}
