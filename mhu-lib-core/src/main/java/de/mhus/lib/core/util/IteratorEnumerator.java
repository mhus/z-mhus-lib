package de.mhus.lib.core.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * <p>IteratorEnumerator class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class IteratorEnumerator<T> implements Enumeration<T>{

	private Iterator<T> iter;
	
	/**
	 * <p>Constructor for IteratorEnumerator.</p>
	 *
	 * @param iter a {@link java.util.Iterator} object.
	 */
	public IteratorEnumerator(Iterator<T> iter) {
		this.iter = iter;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean hasMoreElements() {
		return iter.hasNext();
	}

	/** {@inheritDoc} */
	@Override
	public T nextElement() {
		return iter.next();
	}

}
