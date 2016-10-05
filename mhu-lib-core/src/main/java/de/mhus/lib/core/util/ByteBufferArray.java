/*
 *  Copyright (C) 2002-2004 Mike Hummel
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package de.mhus.lib.core.util;

public class ByteBufferArray {

	int extend = 1024 * 10;
	int next = 0;

	byte[] buffer = null;

	public ByteBufferArray() {
		buffer = new byte[extend];
	}

	public ByteBufferArray(int initial) {
		buffer = new byte[initial];
	}

	public ByteBufferArray(int initial, int extend) {
		buffer = new byte[initial];
		this.extend = extend;
	}

	public void append(byte in) {
		if (next >= buffer.length) {
			byte[] newBuffer = new byte[buffer.length + extend];
			System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
			buffer = newBuffer;
		}
		buffer[next] = in;
		next++;
	}

	public void append(byte[] in) {
		append(in, 0, in.length);
	}

	public void append(byte[] in, int offset, int len) {
		// if ( offset + len > in.length ) len = in.length - offset; // hmmm
		// ....
		if (next + len > buffer.length) {
			byte[] newBuffer = new byte[buffer.length + Math.max(extend, len)];
			System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
			buffer = newBuffer;
		}
		try {
			System.arraycopy(in, offset, buffer, next, len);
		} catch (IndexOutOfBoundsException ioe) {
			System.out.println("FATAL: ByteBufferArray.append: " + in.length
					+ ' ' + offset + ' ' + buffer.length + ' ' + next + ' '
					+ len);
			throw ioe;
		}
		next += len;
	}

	public int getSize() {
		return next;
	}

	public byte[] toByte() {
		byte[] newBuffer = new byte[next];
		System.arraycopy(buffer, 0, newBuffer, 0, next);
		return newBuffer;
	}

	public boolean isCurrentlyFull() {
		return next == buffer.length;
	}

	public byte[] getInternalBuffer() {
		return buffer;
	}

	public void clear() {
		buffer = new byte[0];
		next = 0;
	}

}
