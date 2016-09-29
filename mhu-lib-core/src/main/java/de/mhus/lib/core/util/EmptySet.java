package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>EmptySet class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class EmptySet<T> implements Set<T> {

	/** {@inheritDoc} */
	@Override
	public int size() {
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(Object o) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<T> iterator() {
		return new EmptyIterator<T>();
	}

	/** {@inheritDoc} */
	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public <E> E[] toArray(E[] a) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean add(T e) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean remove(Object o) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
	}

}
