package de.mhus.lib.core.util;

import java.util.ListIterator;

/**
 * <p>ImmutableListIterator class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ImmutableListIterator<E> implements ListIterator<E> {

	private ListIterator<E> parent;

	/**
	 * <p>Constructor for ImmutableListIterator.</p>
	 *
	 * @param parent a {@link java.util.ListIterator} object.
	 */
	public ImmutableListIterator(ListIterator<E> parent) {
		this.parent = parent;
	}

	/** {@inheritDoc} */
	@Override
	public void add(E o) {
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasNext() {
		return parent.hasNext();
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasPrevious() {
		return parent.hasPrevious();
	}

	/** {@inheritDoc} */
	@Override
	public E next() {
		return parent.next();
	}

	/** {@inheritDoc} */
	@Override
	public int nextIndex() {
		return parent.nextIndex();
	}

	/** {@inheritDoc} */
	@Override
	public E previous() {
		return parent.previous();
	}

	/** {@inheritDoc} */
	@Override
	public int previousIndex() {
		return parent.previousIndex();
	}

	/** {@inheritDoc} */
	@Override
	public void remove() {
	}

	/** {@inheritDoc} */
	@Override
	public void set(E o) {
	}

}
