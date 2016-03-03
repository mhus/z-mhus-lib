package de.mhus.lib.jpa;

import java.util.Iterator;

/**
 * <p>JpaIterator class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JpaIterator<T> implements Iterator<T> {

	private JpaEntityManager entityManager;
	private Iterator<? extends Object> iterator;

	/**
	 * <p>Constructor for JpaIterator.</p>
	 *
	 * @param entityManager a {@link de.mhus.lib.jpa.JpaEntityManager} object.
	 * @param iterator a {@link java.util.Iterator} object.
	 */
	public JpaIterator(JpaEntityManager entityManager, Iterator<? extends Object> iterator) {
		this.entityManager = entityManager;
		this.iterator = iterator;
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public T next() {
		return (T)entityManager.injectObject(iterator.next());
	}

	/** {@inheritDoc} */
	@Override
	public void remove() {

	}

}
