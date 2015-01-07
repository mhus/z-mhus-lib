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

package de.mhus.lib.core.io;

import java.io.IOException;
import java.io.Writer;

public class YWriter extends Writer {

	private Writer[] writers;

	public YWriter(Writer[] pWriters) {
		writers = pWriters;
	}

	public void close() throws IOException {
		for (int i = 0; i < writers.length; i++)
			writers[i].close();
	}

	public void flush() throws IOException {
		for (int i = 0; i < writers.length; i++)
			writers[i].flush();
	}

	public void write(char[] cbuf, int off, int len) throws IOException {
		for (int i = 0; i < writers.length; i++)
			writers[i].write(cbuf, off, len);
	}

}
