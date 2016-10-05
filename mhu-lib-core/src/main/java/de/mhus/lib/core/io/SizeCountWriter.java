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

public class SizeCountWriter extends Writer {

	private Writer writer;
	private long count;

	public SizeCountWriter(Writer out) {
		writer = out;
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void write(char[] ac, int i, int j) throws IOException {
		count += j;
		writer.write(ac, i, j);
	}

	public long getSize() {
		return count;
	}

}
