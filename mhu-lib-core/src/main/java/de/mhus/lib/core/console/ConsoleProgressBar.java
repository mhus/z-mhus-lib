/*
 * ./core/de/mhu/lib/console/ProgressBar.java
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

package de.mhus.lib.core.console;



/**
 * @author hummel
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsoleProgressBar {

	int len = 0;
	long max = 0;
	long current = 0;

	long start = -1;
	long stop = -1;

	Console stream;
	
	
	public ConsoleProgressBar(Console console) {
		this(console, 0, 100);
	}
	
	public ConsoleProgressBar(Console console, long _max) {
		this(console, 0, _max);
	}

	public ConsoleProgressBar(Console console, int _len, long _max) {
		this.stream = console;
		if (_len < 1)
			_len = console.getWidth();
		len = _len;
		max = _max;
	}

	public void add(long _add) {
		set(current + _add);
	}

	public void set(long _current) {

		if (stop != -1)
			return;
		if (start == -1) {
			start = System.currentTimeMillis();
			paintHeader();
		}

		if (_current > max)
			_current = max;
		if (_current < 0)
			_current = 0;
		if (_current < current)
			clean();
		paint(_current);
	}

	public void clean() {

		if (stop != -1)
			return;

		current = 0;
		stream.cr();
		for (int i = 0; i < len; i++)
			stream.print(' ');
		stream.cr();
	}

	private void paintHeader() {

		for (int i = 0; i < len; i++) {
			if (i == 0 || i == len - 1) {
				stream.print('|');
			} else if (i % 10 == 0) {
				stream.print('+');
			} else
				stream.print('-');
		}
		stream.println();
	}

	private void paint(long _current) {
		int old = (int) ((double) len / (double) max * (double) current);
		int new_ = (int) ((double) len / (double) max * (double) _current);
		int diff = new_ - old;
		for (int i = 0; i < diff; i++)
			stream.print('*');
		current = _current;
	}

	public void finish() {
		stream.println();
		stop = System.currentTimeMillis();
	}

	public long getTime() {
		if (stop == -1)
			return (System.currentTimeMillis() - start) / 1000;
		else
			return (stop - start) / 1000;
	}

	/**
	 * 
	 * @return Rate per milliseconds
	 */
	public float getRate() {
		long time = getTime();
		if (time == 0)
			return (float) current;
		return (float) current / (float) time;
	}

}
