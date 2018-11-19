package de.mhus.lib.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.mhus.lib.core.util.ByteBuffer;

public class StreamBuffer {
	
	private ByteBuffer buffer = new ByteBuffer();
	private InputStream is = new InputStream() {
		
		@Override
		public int read() throws IOException {
			synchronized (buffer) {
				if (buffer.isEmpty()) return -1;
				return buffer.shift();
			}
		}
	};
	private OutputStream os = new OutputStream() {
		
		@Override
		public void write(int b) throws IOException {
			synchronized (buffer) {
				buffer.append((byte) b);
			}
		}
	};
	
	public InputStream getInputStream() {
		return is;
	}

	public OutputStream getOutputStream() {
		return os;
	}

	public int size() {
		return buffer.size();
	}
	
}
