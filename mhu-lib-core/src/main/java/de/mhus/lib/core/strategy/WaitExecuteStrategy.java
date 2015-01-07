package de.mhus.lib.core.strategy;

import java.util.concurrent.TimeoutException;

import de.mhus.lib.core.MThread;

public class WaitExecuteStrategy extends ExecuteStrategy {

	private Operation executable;
	private long waitTime;
	private long timeout;
	
	@Override
	public void doExecute(TaskContext context) throws Exception {
		if (executable == null) return;
		try {
			long cnt = timeout;;
			while (!executable.setBusy(this)) {
				MThread.sleep(waitTime);
				if (cnt > 0) {
					cnt-=waitTime;
					if (cnt <= 0) throw new TimeoutException("timeout");
				}
			}
			if (executable == null) return;
			executable.doExecute(context);
		} finally {
			if (executable != null) executable.releaseBusy(this);
		}
	}

	@Override
	public Operation getExecutable() {
		return executable;
	}

	@Override
	public void setExecutable(Operation executable) {
		this.executable = executable;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	
}
