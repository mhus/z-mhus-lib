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
import java.io.OutputStream;

/**
 * <p>SizeCountOutputStream class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class SizeCountOutputStream extends OutputStream {

	private long count;
	private OutputStream stream;

	/**
	 * <p>Constructor for SizeCountOutputStream.</p>
	 *
	 * @param stream a {@link java.io.OutputStream} object.
	 */
	public SizeCountOutputStream(OutputStream stream) {
		this.stream = stream;
	}

	/** {@inheritDoc} */
	@Override
	public void write(int b) throws IOException {
		count++;
		stream.write(b);
	}

	/**
	 * <p>getSize.</p>
	 *
	 * @return a long.
	 */
	public long getSize() {
		return count;
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws IOException {
		stream.close();
	}

	/** {@inheritDoc} */
	@Override
	public void flush() throws IOException {
		stream.flush();
	}

}
