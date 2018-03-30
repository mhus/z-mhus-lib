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
package de.mhus.lib.core.util;

import java.util.Iterator;
import java.util.LinkedList;

public class ByteBuffer {

	private int extend = 1024 * 10;

	private LinkedList<byte[]> list = new LinkedList<byte[]>();
	private byte[] last = null;
	private int lastPos = 0;
	private byte[] buffer = new byte[1];
	private int size = 0;

	public ByteBuffer() {
	}

	public ByteBuffer(int extend) {
		if (extend < 1)
			throw new RuntimeException("Extend to less");
		this.extend = extend;
	}

	public void append(byte in) {
		buffer[0] = in;
		append(buffer, 0, 1);
	}

	public void append(byte[] in) {
		append(in, 0, in.length);
	}

	public void append(byte[] in, int offset, int len) {

		if (last == null) {
			last = new byte[Math.max(extend, len)];
			list.addLast(last);
			lastPos = 0;
		}
		while (len > 0) {
			if (len < last.length - lastPos) {
				System.arraycopy(in, offset, last, lastPos, len);
				lastPos += len;
				size += len;
				len = 0;
			} else {
				int max = last.length - lastPos;
				System.arraycopy(in, offset, last, lastPos, max);
				offset += max;
				len -= max;
				size += max;
				last = null;
			}
		}

	}

	public int getSize() {
		return size;
	}

	public byte[] toByte() {
		byte[] out = new byte[size];
		int pos = 0;
		for (Iterator<byte[]> i = list.iterator(); i.hasNext();) {
			byte[] part = i.next();
			int len = part.length;
			if (pos + len >= out.length)
				len = out.length - pos;
			System.arraycopy(part, 0, out, pos, len);
		}
		return out;
	}

	public void clear() {
		list.clear();
		size = 0;
	}

}
