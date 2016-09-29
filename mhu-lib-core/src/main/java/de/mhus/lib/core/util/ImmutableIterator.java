package de.mhus.lib.core.util;

import java.util.Iterator;

/**
 * <p>ImmutableIterator class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ImmutableIterator<E> implements Iterator<E> {

	private Iterator<E> parent;

	/**
	 * <p>Constructor for ImmutableIterator.</p>
	 *
	 * @param parent a {@link java.util.Iterator} object.
	 */
	public ImmutableIterator(Iterator<E> parent) {
		this.parent = parent;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasNext() {
		return parent.hasNext();
	}

	/** {@inheritDoc} */
	@Override
	public E next() {
		return parent.next();
	}

	/** {@inheritDoc} */
	@Override
	public void remove() {
	}

}
