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

package de.mhus.lib.errors;

import java.util.UUID;

public class MException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private UUID errorId = UUID.randomUUID();
	
	public MException(Object ... in) {
		super(argToString(in),argToCause(in));
	}
	
	@Override
	public String toString() {
		return errorId.toString() + " " + super.toString();
	}
	
	public static String argToString(Object ... in) {
		StringBuffer sb = new StringBuffer();
		for (Object o : in) {
			if (o instanceof Object[]) {
				sb.append("[");
				sb.append(argToString(o));
				sb.append("]");
			} else
				sb.append("[").append(o).append("]");
		}
		return sb.toString();
	}
	
	public static Throwable argToCause(Object ... in) {
		Throwable cause = null;
		for (Object o : in) {
			if ((o instanceof Throwable) && cause == null) {
				cause = (Throwable)o;
			} else
			if (o instanceof Object[]) {
				cause = argToCause(o);
				if (cause != null) return cause;
			}
		}
		return cause;
	}
	
	public UUID getId() {
		return errorId;
	}
	
}
