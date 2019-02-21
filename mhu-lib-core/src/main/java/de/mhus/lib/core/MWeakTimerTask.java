/**
w * Copyright 2018 Mike Hummel
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

import java.lang.ref.WeakReference;
import java.util.TimerTask;

import de.mhus.lib.basics.Named;

public class MWeakTimerTask extends TimerTask implements Named {

	private boolean canceled = false;
	private String name;
	private WeakReference<TimerTask> task;
	
	public MWeakTimerTask(TimerTask task) {
		this.task = new WeakReference<>(task);
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

	protected void doit() throws Exception {
		if (task == null) {
			cancel();
			return;
		}
		TimerTask taskTask = task.get();
		if (taskTask == null || MTimerTask.getStatus(taskTask) == MTimerTask.CANCELLED ) {
			cancel();
			return;
		}
		
		taskTask.run();
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

}
