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
import java.util.TimerTask;

import de.mhus.lib.core.logging.MLogUtil;

public abstract class MTimerTask extends TimerTask implements ITimerTask {

    public static final int UNKNOWN = -1;
    public static final int VIRGIN = 0;
    public static final int SCHEDULED   = 1;
    public static final int EXECUTED    = 2;
    public static final int CANCELLED   = 3;

	private boolean canceled = false;
	private String name;
	protected Object environment;
	
	public MTimerTask() {
		setName(MSystem.getClassName(this));
	}
	
	@Override
	final public void run() {
		run(null);
	}
	
	@Override
	final public void run(Object environment) {
		boolean error = false;
		try {
			this.environment = environment;
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
		this.environment = null;
	}

	@Override
	public void onError(Throwable t) {
		t.printStackTrace();
	}

	@Override
	public void onFinal(boolean isError) {
	}

	protected abstract void doit() throws Exception;
	
    @Override
    public boolean cancel() {
    	setCanceled(true);
    	return super.cancel();
    }
    
	@Override
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
			Class<? extends TimerTask> clazz = task.getClass();
			Field field = MSystem.getDeclaredField(clazz,"state");
			if (field != null) {
				if (!field.isAccessible())
					field.setAccessible(true);
				return field.getInt(task);
			}
		} catch (Throwable t) {
			MLogUtil.log().d(task,t);
		}
		return -1;
	}
	
}
