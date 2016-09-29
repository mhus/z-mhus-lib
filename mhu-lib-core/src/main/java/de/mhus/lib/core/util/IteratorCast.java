package de.mhus.lib.core.util;

import java.util.Iterator;

/**
 * <p>IteratorCast class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class IteratorCast<F,T> implements Iterator<T> {

	private Iterator<F> from;

	/**
	 * <p>Constructor for IteratorCast.</p>
	 *
	 * @param from a {@link java.util.Iterator} object.
	 */
	public IteratorCast(Iterator<F> from) {
		this.from = from;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasNext() {
		return from.hasNext();
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public T next() {
		return (T)from.next();
	}

	/** {@inheritDoc} */
	@Override
	public void remove() {
		from.remove();
	}

}
