/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

import de.mhus.lib.core.MThread;

public class PipedStream {

	private Out out = new Out();
	private In in = new In();
	private LinkedList<Byte> buffer = new LinkedList<Byte>();
	private int maxBufferSize = 3000;
	private long writeTimeout = -1;
	private long readTimeout = -1;

	public OutputStream getOut() {
		return out;
	}
	
	public InputStream getIn() {
		return in;
	}
	
	public int getBufferSize() {
		return buffer.size();
	}
	
	public void setMaxBufferSize(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}

	public int getMaxBufferSize() {
		return maxBufferSize;
	}

	public void setWriteTimeout(long writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public long getWriteTimeout() {
		return writeTimeout;
	}

	public void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

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
