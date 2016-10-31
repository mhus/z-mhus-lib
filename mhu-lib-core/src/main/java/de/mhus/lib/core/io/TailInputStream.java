package de.mhus.lib.core.io;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TailInputStream extends InputStream {

	private File file;
	private long pos;
	private FileInputStream is;
	private boolean closed = false;

	public TailInputStream(File file) throws IOException {
		this.file = file;
		clean();
	}
	
	@Override
	public int read() throws IOException {
		
		if (closed) throw new EOFException();
		
		long size = file.length();
		while (size == pos) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				throw new EOFException();
			}
			if (Thread.currentThread().isInterrupted()) throw new EOFException();
			if (closed) throw new EOFException();
			size = file.length();
		}
		if (size < pos) {
			pos = 0;
			try {
				is.close();
			} catch (Throwable t) {}
			is = new FileInputStream(file);
		} 
		int ret = is.read();
		if (ret >= 0) pos++;
		return ret;
	}
	
	public long delta() {
		long size = file.length();
		if (size > pos) return size-pos;
		return size;
	}
	
	public int available() {
		long size = delta();
		if (size > Integer.MAX_VALUE) return Integer.MAX_VALUE;
		return (int)size;
	}
	
	@Override
	public void close() throws IOException {
		closed = true;
		if (is != null) is.close();
		super.close();
	}
	
	public void clean() throws IOException {
		this.pos = file.length();
		try {
			if (is != null) is.close();
		} catch (Throwable t) {}
		is = new FileInputStream(file);
		is.skip(pos);
	}

}
