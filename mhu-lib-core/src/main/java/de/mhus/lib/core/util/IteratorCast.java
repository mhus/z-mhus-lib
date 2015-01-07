package de.mhus.lib.core.util;

import java.util.Iterator;

public class IteratorCast<F,T> implements Iterator<T> {

	private Iterator<F> from;

	public IteratorCast(Iterator<F> from) {
		this.from = from;
	}
	
	@Override
	public boolean hasNext() {
		return from.hasNext();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T next() {
		return (T)from.next();
	}

	@Override
	public void remove() {
		from.remove();
	}

}
