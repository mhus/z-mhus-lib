package de.mhus.lib.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import de.mhus.lib.core.MThread;

/**
 * <p>PipedStream class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class PipedStream {

	private Out out = new Out();
	private In in = new In();
	private LinkedList<Byte> buffer = new LinkedList<Byte>();
	private int maxBufferSize = 3000;
	private long writeTimeout = -1;
	private long readTimeout = -1;

	/**
	 * <p>Getter for the field <code>out</code>.</p>
	 *
	 * @return a {@link java.io.OutputStream} object.
	 */
	public OutputStream getOut() {
		return out;
	}
	
	/**
	 * <p>Getter for the field <code>in</code>.</p>
	 *
	 * @return a {@link java.io.InputStream} object.
	 */
	public InputStream getIn() {
		return in;
	}
	
	/**
	 * <p>getBufferSize.</p>
	 *
	 * @return a int.
	 */
	public int getBufferSize() {
		return buffer.size();
	}
	
	/**
	 * <p>Setter for the field <code>maxBufferSize</code>.</p>
	 *
	 * @param maxBufferSize a int.
	 */
	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	/**
	 * <p>Getter for the field <code>maxBufferSize</code>.</p>
	 *
	 * @return a int.
	 */
	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	/**
	 * <p>Setter for the field <code>writeTimeout</code>.</p>
	 *
	 * @param writeTimeout a long.
	 */
	public void setWriteTimeout(long writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	/**
	 * <p>Getter for the field <code>writeTimeout</code>.</p>
	 *
	 * @return a long.
	 */
	public long getWriteTimeout() {
		return writeTimeout;
	}

	/**
	 * <p>Setter for the field <code>readTimeout</code>.</p>
	 *
	 * @param readTimeout a long.
	 */
	public void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * <p>Getter for the field <code>readTimeout</code>.</p>
	 *
	 * @return a long.
	 */
	public long getReadTimeout() {
		return readTimeout;
	}

	private class Out extends OutputStream {

		@Override
		public void write(int b) throws IOException {
			
			synchronized(this) {
				long time = 0;
				while (buffer.size() >= maxBufferSize) {
					if (getWriteTimeout() >=0 && time >= getWriteTimeout())
						throw new IOException("timeout", null);
					MThread.sleep(100);
					if (getWriteTimeout() >= 0) time+=100;
				}
				
				synchronized (buffer) {
					buffer.addLast((byte)b);
				}
			}
		}
		
	}
	
	private class In extends InputStream {
		
		@Override
		public int read() throws IOException {

			synchronized(this) {
				long time = 0;
				while (buffer.size() == 0) {
					if (getReadTimeout() >=0 && time >= getReadTimeout())
						throw new IOException("timeout", null);
					MThread.sleep(100);
					if (getReadTimeout() >= 0) time+=100;
				}
				
				synchronized (buffer) {
					Byte first = buffer.removeFirst();
					return first;
				}
			}
		}
		
	}
	
	
}
