package de.mhus.lib.core.util;

import java.util.Iterator;

public class ImmutableIterator<E> implements Iterator<E> {

	private Iterator<E> parent;

	public ImmutableIterator(Iterator<E> parent) {
		this.parent = parent;
	}

	public boolean hasNext() {
		return parent.hasNext();
	}

	public E next() {
		return parent.next();
	}

	public void remove() {
	}

}
