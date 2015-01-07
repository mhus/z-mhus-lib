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

public class Rot13 {

	public static String encode(String in) {
		return decode(in);
	}

	public static String decode(String in) {

		StringBuffer out = new StringBuffer();

		for (int i = 0; i < in.length(); i++) {
			int chr = in.charAt(i);

			// convert char if required
			if ((chr >= 'A') && (chr <= 'Z')) {
				chr += 13;
				if (chr > 'Z')
					chr -= 26;
			} else if ((chr >= 'a') && (chr <= 'z')) {
				chr += 13;
				if (chr > 'z')
					chr -= 26;
			} else if ((chr >= '0') && (chr <= '9')) {
				chr += 5;
				if (chr > '9')
					chr -= 10;
			}

			// and return it to sender
			out.append((char) chr);
		}

		return out.toString();
	}

}
