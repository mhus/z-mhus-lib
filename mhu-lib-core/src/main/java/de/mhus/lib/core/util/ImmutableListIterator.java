package de.mhus.lib.core.util;

import java.util.ListIterator;

public class ImmutableListIterator<E> implements ListIterator<E> {

	private ListIterator<E> parent;

	public ImmutableListIterator(ListIterator<E> parent) {
		this.parent = parent;
	}

	@Override
	public void add(E o) {
	}

	@Override
	public boolean hasNext() {
		return parent.hasNext();
	}

	@Override
	public boolean hasPrevious() {
		return parent.hasPrevious();
	}

	@Override
	public E next() {
		return parent.next();
	}

	@Override
	public int nextIndex() {
		return parent.nextIndex();
	}

	@Override
	public E previous() {
		return parent.previous();
	}

	@Override
	public int previousIndex() {
		return parent.previousIndex();
	}

	@Override
	public void remove() {
	}

	@Override
	public void set(E o) {
	}

}
