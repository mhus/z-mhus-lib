/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
