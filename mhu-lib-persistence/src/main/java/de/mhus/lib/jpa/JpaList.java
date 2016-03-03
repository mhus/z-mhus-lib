package de.mhus.lib.jpa;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>JpaList class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class JpaList<T> implements List<T> {

	private JpaEntityManager entityManager;
	private List<? extends Object> list;

	/**
	 * <p>Constructor for JpaList.</p>
	 *
	 * @param entityManager a {@link de.mhus.lib.jpa.JpaEntityManager} object.
	 * @param list a {@link java.util.List} object.
	 */
	public JpaList(JpaEntityManager entityManager, List<? extends Object> list) {
		this.entityManager = entityManager;
		this.list = list;
	}

	/** {@inheritDoc} */
	@Override
	public boolean add(Object arg0) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void add(int arg0, Object arg1) {
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void clear() {
	}

	/** {@inheritDoc} */
	@Override
	public boolean contains(Object arg0) {
		return list.contains(arg0);
	}

	/** {@inheritDoc} */
	@Override
	public boolean containsAll(Collection<?> arg0) {
		return list.containsAll(arg0);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public T get(int arg0) {
		return (T)entityManager.injectObject(list.get(arg0));
	}

	/** {@inheritDoc} */
	@Override
	public int indexOf(Object arg0) {
		return list.indexOf(arg0);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public Iterator<T> iterator() {
		return new JpaIterator<T>(entityManager,list.iterator());
	}

	/** {@inheritDoc} */
	@Override
	public int lastIndexOf(Object arg0) {
		return list.lastIndexOf(arg0);
	}

	/** {@inheritDoc} */
	@Override
	public ListIterator<T> listIterator() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public ListIterator<T> listIterator(int arg0) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean remove(Object arg0) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public T remove(int arg0) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean removeAll(Collection<?> arg0) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean retainAll(Collection<?> arg0) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public T set(int arg0, Object arg1) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public int size() {
		return list.size();
	}

	/** {@inheritDoc} */
	@Override
	public List<T> subList(int arg0, int arg1) {
		return new JpaList<T>(entityManager, list.subList(arg0, arg1));
	}

	/** {@inheritDoc} */
	@Override
	public Object[] toArray() {
		Object[] out = list.toArray();
		if (out != null)
			for (Object o : out)
				entityManager.injectObject(o);
		return out;
	}

	/** {@inheritDoc} */
	@Override
	public <E> E[] toArray(E[] arg0) {
		E[] out = list.toArray(arg0);
		if (out != null)
			for (Object o : out)
				entityManager.injectObject(o);
		return out;
	}

}
