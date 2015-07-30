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

import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

public abstract class MTimerTask extends TimerTask implements Observer {

	@Override
	final public void run() {
		boolean error = false;
		try {
			doit();
		} catch (Throwable t) {
			try {
				onError(t);
			} catch (Throwable t1) {
			}
			error = true;
		}
		try {
			onFinal(error);
		} catch (Throwable t) {
		}
	}

	protected void onError(Throwable t) {
		t.printStackTrace();
	}

	protected void onFinal(boolean isError) {
	}

	public abstract void doit() throws Exception;
	
    @Override
	public void update(Observable o, Object arg) {
    	run();
    }

}
