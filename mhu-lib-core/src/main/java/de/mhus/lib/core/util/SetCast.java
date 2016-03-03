package de.mhus.lib.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>SetCast class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class SetCast<F,T> implements Set<T> {

	private Set<F> from;

	/**
	 * <p>Constructor for SetCast.</p>
	 *
	 * @param from a {@link java.util.Set} object.
	 */
	public SetCast(Set<F> from) {
		this.from = from;
	}
	
	/** {@inheritDoc} */
	@Override
	public int size() {
		return from.size();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return from.isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(Object o) {
		return from.contains(o);
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<T> iterator() {
		return new IteratorCast<F, T>(from.iterator());
	}

	/** {@inheritDoc} */
	@Override
	public Object[] toArray() {
		return from.toArray();
	}

	/** {@inheritDoc} */
	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return from.toArray(a);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public boolean add(T e) {
		return from.add((F)e);
	}

	/** {@inheritDoc} */
	@Override
	public boolean remove(Object o) {
		return from.remove(o);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsAll(Collection<?> c) {
		return from.containsAll(c);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return from.addAll((Collection<? extends F>) c);
	}

	/** {@inheritDoc} */
	@Override
	public boolean retainAll(Collection<?> c) {
		return from.retainAll(c);
	}

	/** {@inheritDoc} */
	@Override
	public boolean removeAll(Collection<?> c) {
		return from.removeAll(c);
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
		from.clear();
	}

}
