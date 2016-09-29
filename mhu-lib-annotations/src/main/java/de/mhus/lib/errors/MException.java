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

/**
 * <p>MException class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7100404828615087028L;
	
	private UUID errorId = UUID.randomUUID();
	
	/**
	 * <p>Constructor for MException.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 */
	public MException(Object ... in) {
		super(argToString(in),argToCause(in));
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return errorId.toString() + " " + super.toString();
	}
	
	/**
	 * <p>argToString.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 */
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
	
	/**
	 * <p>argToCause.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 * @return a {@link java.lang.Throwable} object.
	 */
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
	
	/**
	 * <p>getId.</p>
	 *
	 * @return a {@link java.util.UUID} object.
	 */
	public UUID getId() {
		return errorId;
	}
	
}
