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

package de.mhus.lib.core;

import java.util.Timer;

import de.mhus.lib.core.lang.IBase;

public class MTimer extends Timer implements IBase {

	public MTimer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MTimer(boolean isDaemon) {
		super(isDaemon);
		// TODO Auto-generated constructor stub
	}

	public MTimer(String name, boolean isDaemon) {
		super(name, isDaemon);
		// TODO Auto-generated constructor stub
	}

	public MTimer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

}
