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

import java.util.Iterator;
import java.util.LinkedList;

/**
 * <p>ByteBuffer class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class ByteBuffer {

	private int extend = 1024 * 10;

	private LinkedList<byte[]> list = new LinkedList<byte[]>();
	private byte[] last = null;
	private int lastPos = 0;
	private byte[] buffer = new byte[1];
	private int size = 0;

	/**
	 * <p>Constructor for ByteBuffer.</p>
	 */
	public ByteBuffer() {
	}

	/**
	 * <p>Constructor for ByteBuffer.</p>
	 *
	 * @param extend a int.
	 */
	public ByteBuffer(int extend) {
		if (extend < 1)
			throw new RuntimeException("Extend to less");
		this.extend = extend;
	}

	/**
	 * <p>append.</p>
	 *
	 * @param in a byte.
	 */
	public void append(byte in) {
		buffer[0] = in;
		append(buffer, 0, 1);
	}

	/**
	 * <p>append.</p>
	 *
	 * @param in an array of byte.
	 */
	public void append(byte[] in) {
		append(in, 0, in.length);
	}

	/**
	 * <p>append.</p>
	 *
	 * @param in an array of byte.
	 * @param offset a int.
	 * @param len a int.
	 */
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

	/**
	 * <p>Getter for the field <code>size</code>.</p>
	 *
	 * @return a int.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * <p>toByte.</p>
	 *
	 * @return an array of byte.
	 */
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

	/**
	 * <p>clear.</p>
	 */
	public void clear() {
		list.clear();
		size = 0;
	}

}
