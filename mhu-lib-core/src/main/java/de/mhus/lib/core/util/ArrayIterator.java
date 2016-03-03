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

	/**
	 * <p>Constructor for ArrayIterator.</p>
	 *
	 * @param from an array of T objects.
	 */
	public ArrayIterator(T[] from) {
		this.from = from;
		pos = 0;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasNext() {
		return pos < from.length;
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
