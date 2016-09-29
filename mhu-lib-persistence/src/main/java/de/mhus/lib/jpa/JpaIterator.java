package de.mhus.lib.jpa;

import java.util.Iterator;

public class JpaIterator<T> implements Iterator<T> {

	private JpaEntityManager entityManager;
	private Iterator<? extends Object> iterator;

	public JpaIterator(JpaEntityManager entityManager, Iterator<? extends Object> iterator) {
		this.entityManager = entityManager;
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T next() {
		return (T)entityManager.injectObject(iterator.next());
	}

	@Override
	public void remove() {

	}

}
