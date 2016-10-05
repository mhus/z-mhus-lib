package de.mhus.lib.core.util;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {

	private T[] from;
	private int pos;
	private int max;

	public ArrayIterator(T[] from, int start, int stop) {
		this.from = from;
		pos = Math.max(start, 0);
		max = Math.min(stop, from.length);
	}
	
	public ArrayIterator(T[] from) {
		this.from = from;
		pos = 0;
		max = from.length;
	}
	
	@Override
	public boolean hasNext() {
		return pos < max;
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
