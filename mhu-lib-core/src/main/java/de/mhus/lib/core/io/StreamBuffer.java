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
