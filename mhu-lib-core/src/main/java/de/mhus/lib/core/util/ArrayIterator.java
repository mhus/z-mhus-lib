package de.mhus.lib.core.util;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {

	private T[] from;
	private int pos;

	public ArrayIterator(T[] from) {
		this.from = from;
		pos = 0;
	}
	
	@Override
	public boolean hasNext() {
		return pos < from.length;
	}

	@Override
	public T next() {
		pos++;
		return from[pos-1];
	}

	@Override
	public void remove() {
		
	}

}
