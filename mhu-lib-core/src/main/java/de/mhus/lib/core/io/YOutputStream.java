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
 * <p>YOutputStream class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class YOutputStream extends OutputStream {

	private OutputStream[] streams;

	/**
	 * <p>Constructor for YOutputStream.</p>
	 *
	 * @param pStreams an array of {@link java.io.OutputStream} objects.
	 */
	public YOutputStream(OutputStream[] pStreams) {
		streams = pStreams;
	}

	/** {@inheritDoc} */
	@Override
	public void write(int b) throws IOException {
		for (int i = 0; i < streams.length; i++)
			streams[i].write(b);
	}

	/** {@inheritDoc} */
	@Override
	public void write(byte b[]) throws IOException {
		for (int i = 0; i < streams.length; i++)
			streams[i].write(b);
	}

	/** {@inheritDoc} */
	@Override
	public void write(byte b[], int off, int len) throws IOException {
		for (int i = 0; i < streams.length; i++)
			streams[i].write(b, off, len);
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws IOException {
		for (int i = 0; i < streams.length; i++)
			streams[i].close();
	}

}
