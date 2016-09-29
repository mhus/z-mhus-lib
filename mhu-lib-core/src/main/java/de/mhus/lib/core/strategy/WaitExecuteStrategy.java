package de.mhus.lib.core.strategy;

import java.util.concurrent.TimeoutException;

import de.mhus.lib.core.MThread;

/**
 * <p>WaitExecuteStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class WaitExecuteStrategy extends ExecuteStrategy {

	private Operation executable;
	private long waitTime;
	private long timeout;
	
	/** {@inheritDoc} */
	@Override
	protected OperationResult doExecute2(TaskContext context) throws Exception {
		if (executable == null) return new NotSuccessful(this, "executable not found", OperationResult.EMPTY);
		try {
			long cnt = timeout;;
			while (!executable.setBusy(this)) {
				MThread.sleep(waitTime);
				if (cnt > 0) {
					cnt-=waitTime;
					if (cnt <= 0) throw new TimeoutException("timeout");
				}
			}
			if (executable == null) return new NotSuccessful(this, "executable not found", OperationResult.EMPTY);
			return executable.doExecute(context);
		} finally {
			if (executable != null) executable.releaseBusy(this);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Operation getExecutable() {
		return executable;
	}

	/** {@inheritDoc} */
	@Override
	public void setExecutable(Operation executable) {
		this.executable = executable;
	}

	/**
	 * <p>Getter for the field <code>waitTime</code>.</p>
	 *
	 * @return a long.
	 */
	public long getWaitTime() {
		return waitTime;
	}

	/**
	 * <p>Setter for the field <code>waitTime</code>.</p>
	 *
	 * @param waitTime a long.
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	/** {@inheritDoc} */
	@Override
	public OperationDescription getDescription() {
		if (executable == null) return null;
		return executable.getDescription();
	}

	/** {@inheritDoc} */
	@Override
	public boolean canExecute(TaskContext context) {
		if (executable == null) return false;
		return executable.canExecute(context);
	}

	/** {@inheritDoc} */
	@Override
	public boolean hasAccess() {
		if (executable == null) return false;
		return executable.hasAccess();
	}

}
