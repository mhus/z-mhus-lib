package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>ArraySet class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ArraySet<T> implements Set<T> {

	private T[] from;

	/**
	 * <p>Constructor for ArraySet.</p>
	 *
	 * @param from an array of T objects.
	 */
	public ArraySet(T[] from) {
		this.from = from;
	}
	
	/** {@inheritDoc} */
	@Override
	public int size() {
		return from.length;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(Object o) {
		for (T t : from) if (t.equals(o)) return true;
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator<T>(from);
	}

	/** {@inheritDoc} */
	@Override
	public Object[] toArray() {
		return from;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public <A> A[] toArray(A[] a) {
		return (A[]) from;
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
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
