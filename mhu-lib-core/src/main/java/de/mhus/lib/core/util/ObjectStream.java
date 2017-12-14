package de.mhus.lib.core.util;

import java.util.Iterator;

import de.mhus.lib.core.logging.MLogUtil;

public abstract class ObjectStream<T> implements Iterable<T> {
	
	private T next = null;
	
	private Iterator<T> iterator = new Iterator<T>() {

		@Override
		public boolean hasNext() {
			return ObjectStream.this.hasNext();
		}

		@Override
		public T next() {
			if (next == null) return null;
			T result = next;
			findNext();
			return result;
		}
		
	};
	
	public ObjectStream() {
		findNext();
	}
	
	private void findNext() {
		try {
			next = getNext();
		} catch (Throwable t) {
			MLogUtil.log().d(t);
			next = null;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return iterator;
	}

	public boolean hasNext() {
		return next != null;
	}
	
	/**
	 * Return the next object or null if the end of the stream is reached.
	 * 
	 * @return
	 */
	protected abstract T getNext();
	
}
