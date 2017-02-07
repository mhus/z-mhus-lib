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

import java.lang.reflect.Field;
import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

import de.mhus.lib.basics.Named;
import de.mhus.lib.core.logging.MLogUtil;

public abstract class MTimerTask extends TimerTask implements Observer, Named {

    public static final int UNKNOWN = -1;
    public static final int VIRGIN = 0;
    public static final int SCHEDULED   = 1;
    public static final int EXECUTED    = 2;
    public static final int CANCELLED   = 3;

	private boolean canceled = false;
	private String name;
	
	public MTimerTask() {
		setName(MSystem.getClassName(this));
	}
		
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

    @Override
    public boolean cancel() {
    	setCanceled(true);
    	return super.cancel();
    }
    
	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static int getStatus(TimerTask task) {
		if (task == null) return -1;
		try {
			Field field = task.getClass().getField("state");
			if (!field.isAccessible())
				field.setAccessible(true);
			return field.getInt(task);
		} catch (Throwable t) {
			MLogUtil.log().d(t);
			return -1;
		}
	}
	
}
