package de.mhus.lib.core.util;

import java.util.Iterator;

/**
 * <p>ArrayIterator class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ArrayIterator<T> implements Iterator<T> {

	private T[] from;
	private int pos;
	private int max;

	/**
	 * <p>Constructor for ArrayIterator.</p>
	 *
	 * @param from an array of T objects.
	 * @param start a int.
	 * @param stop a int.
	 */
	public ArrayIterator(T[] from, int start, int stop) {
		this.from = from;
		pos = Math.max(start, 0);
		max = Math.min(stop, from.length);
	}
	
	/**
	 * <p>Constructor for ArrayIterator.</p>
	 *
	 * @param from an array of T objects.
	 */
	public ArrayIterator(T[] from) {
		this.from = from;
		pos = 0;
		max = from.length;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasNext() {
		return pos < max;
	}

	/** {@inheritDoc} */
	@Override
	public T next() {
		pos++;
		return from[pos-1];
	}

	/** {@inheritDoc} */
	@Override
	public void remove() {
		
	}

}
