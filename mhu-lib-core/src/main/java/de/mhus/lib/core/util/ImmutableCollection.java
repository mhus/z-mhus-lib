package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p>ImmutableCollection class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ImmutableCollection<E> implements Collection<E> {

	private Collection<E> parent;

	/**
	 * <p>Constructor for ImmutableCollection.</p>
	 *
	 * @param parent a {@link java.util.Collection} object.
	 */
	public ImmutableCollection(Collection<E> parent) {
		this.parent = parent;
	}

	/** {@inheritDoc} */
	@Override
	public boolean add(E o) {
		return false;
	}

	/**
	 * <p>add.</p>
	 *
	 * @param index a int.
	 * @param element a E object.
	 */
	public void add(int index, E element) {
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		return false;
	}

	/**
	 * <p>addAll.</p>
	 *
	 * @param index a int.
	 * @param c a {@link java.util.Collection} object.
	 * @return a boolean.
	 */
	public boolean addAll(int index, Collection<? extends E> c) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(Object o) {
		return parent.contains(o);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsAll(Collection<?> c) {
		return parent.containsAll(c);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return parent.isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<E> iterator() {
		return new ImmutableIterator<E>(parent.iterator());
	}

	/** {@inheritDoc} */
	@Override
	public boolean remove(Object o) {
		return false;
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param index a int.
	 * @return a E object.
	 */
	public E remove(int index) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	/**
	 * <p>set.</p>
	 *
	 * @param index a int.
	 * @param element a E object.
	 * @return a E object.
	 */
	public E set(int index, E element) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return parent.size();
	}

	/** {@inheritDoc} */
	@Override
	public Object[] toArray() {
		return parent.toArray();
	}

	/** {@inheritDoc} */
	@Override
	public <T> T[] toArray(T[] a) {
		return parent.toArray(a);
	}

}
