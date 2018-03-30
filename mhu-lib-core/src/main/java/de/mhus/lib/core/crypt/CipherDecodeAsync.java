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
package de.mhus.lib.core.crypt;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.LinkedList;

import de.mhus.lib.core.MMath;
import de.mhus.lib.core.MThread;

public class CipherDecodeAsync extends InputStream {

	private AsyncKey key;
	private LinkedList<Byte> buffer = new LinkedList<>();
	private boolean closed = false;

	public CipherDecodeAsync(AsyncKey key) {
		this.key = key;
	}

	public void write(BigInteger next) throws IOException {
		next = MCrypt.decode(key, next);
		byte[] bigEndian = next.toByteArray();
		int size = MMath.unsignetByteToInt(bigEndian[bigEndian.length-1]);
		synchronized (buffer) {
			for (int i = 1; i <= size; i++)
				buffer.add(bigEndian[bigEndian.length-i-1]);
		}
	}
	
	
	@Override
	public int read() throws IOException {
		while (true) {
			synchronized (buffer) {
				if (buffer.size() != 0) break;
			}
			if (closed) return -1;
			MThread.sleep(500);
		}
		synchronized (buffer) {
			return buffer.removeFirst();
		}
	}

    @Override
	public void close() throws IOException {
    	closed  = true;
    }

	public byte[] toBytes() {
		synchronized (buffer) {
			byte[] out = new byte[buffer.size()];
			int cnt = 0;
			for (Byte b : buffer)
				out[cnt++] = b;
			return out;
		}
	}

}
