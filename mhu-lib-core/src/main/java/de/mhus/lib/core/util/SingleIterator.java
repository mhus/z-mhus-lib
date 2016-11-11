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
import java.util.ListIterator;

import de.mhus.lib.basics.ReadOnly;

public class SingleIterator<E> implements Iterator<E>, ListIterator<E>, ReadOnly {

	private E element = null;

	public SingleIterator(E element) {
		this.element = element;
	}

	@Override
	public boolean hasNext() {
		return (element != null);
	}

	@Override
	public E next() {
		E next = element;
		element = null;
		return next;
	}

	@Override
	public void remove() {
	}

	@Override
	public boolean hasPrevious() {
		return false;
	}

	@Override
	public E previous() {
		return null;
	}

	@Override
	public int nextIndex() {
		return hasNext() ? 0 : -1;
	}

	@Override
	public int previousIndex() {
		return -1;
	}

	@Override
	public void set(E e) {
	}

	@Override
	public void add(E e) {
	}

}
