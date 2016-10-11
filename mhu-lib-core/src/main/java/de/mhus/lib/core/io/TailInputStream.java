package de.mhus.lib.core.io;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.mhus.lib.core.MThread;

public class TailInputStream extends InputStream {

	private File file;
	private long pos;
	private FileInputStream is;
	private boolean closed = false;

	public TailInputStream(File file) throws IOException {
		this.file = file;
		toEnd();
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
	
	@Override
	public void close() throws IOException {
		closed = true;
		super.close();
	}
	
	public void toEnd() throws IOException {
		this.pos = file.length();
		try {
			if (is != null) is.close();
		} catch (Throwable t) {}
		is = new FileInputStream(file);
		is.skip(pos);
	}

}
