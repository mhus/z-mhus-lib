package de.mhus.lib.core.util;

import java.util.ListIterator;

public class EmptyListOperator<E> implements ListIterator<E> {

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public E next() {
		return null;
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}

	@Override
	public E previous() {
		return null;
	}

	@Override
	public int nextIndex() {
		return -1;
	}

	@Override
	public int previousIndex() {
		return -1;
	}

	@Override
	public void remove() {
	}

	@Override
	public void set(E e) {
	}

	@Override
	public void add(E e) {
	}

}
