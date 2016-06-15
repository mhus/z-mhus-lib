package de.mhus.lib.core.util;

import java.util.Enumeration;
import java.util.Iterator;

public class IteratorEnumerator<T> implements Enumeration<T>{

	private Iterator<T> iter;
	
	public IteratorEnumerator(Iterator<T> iter) {
		this.iter = iter;
	}
	
	@Override
	public boolean hasMoreElements() {
		return iter.hasNext();
	}

	@Override
	public T nextElement() {
		return iter.next();
	}

}
