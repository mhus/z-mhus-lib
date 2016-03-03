package de.mhus.lib.core.parser;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

/**
 * <p>ParseReader class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ParseReader {

	private Reader str;
	private char current;
	private boolean closed = false;
	private int pos = -1;

	/**
	 * <p>Constructor for ParseReader.</p>
	 *
	 * @param str a {@link java.io.Reader} object.
	 * @throws java.io.IOException if any.
	 */
	public ParseReader(Reader str) throws IOException {
		this.str = str;
		nextCurrent();
	}
	
	private void nextCurrent() throws IOException {
		if (closed) return;
		int read = str.read();
		pos++;
		if (read == -1) {
			closed = true;
		}
		current = (char)read;
	}

	/**
	 * <p>character.</p>
	 *
	 * @return a char.
	 * @throws java.io.EOFException if any.
	 */
	public char character() throws EOFException {
		if (closed) throw new EOFException();
		return current;
	}
	
	/**
	 * <p>consume.</p>
	 *
	 * @throws java.io.IOException if any.
	 */
	public void consume() throws IOException {
		nextCurrent();
	}
	
	/**
	 * <p>isClosed.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * <p>getPosition.</p>
	 *
	 * @return a int.
	 */
	public int getPosition() {
		return pos;
	}
	
}
