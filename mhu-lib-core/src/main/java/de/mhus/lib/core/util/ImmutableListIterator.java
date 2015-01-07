package de.mhus.lib.core.util;

import java.util.ListIterator;

public class ImmutableListIterator<E> implements ListIterator<E> {

	private ListIterator<E> parent;

	public ImmutableListIterator(ListIterator<E> parent) {
		this.parent = parent;
	}

	public void add(E o) {
	}

	public boolean hasNext() {
		return parent.hasNext();
	}

	public boolean hasPrevious() {
		return parent.hasPrevious();
	}

	public E next() {
		return parent.next();
	}

	public int nextIndex() {
		return parent.nextIndex();
	}

	public E previous() {
		return parent.previous();
	}

	public int previousIndex() {
		return parent.previousIndex();
	}

	public void remove() {
	}

	public void set(E o) {
	}

}
