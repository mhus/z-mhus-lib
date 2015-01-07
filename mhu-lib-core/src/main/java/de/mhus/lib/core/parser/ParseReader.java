package de.mhus.lib.core.parser;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class ParseReader {

	private Reader str;
	private char current;
	private boolean closed = false;
	private int pos = -1;

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

	public char character() throws EOFException {
		if (closed) throw new EOFException();
		return current;
	}
	
	public void consume() throws IOException {
		nextCurrent();
	}
	
	public boolean isClosed() {
		return closed;
	}

	public int getPosition() {
		return pos;
	}
	
}
