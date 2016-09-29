package de.mhus.lib.cao;

public abstract class CaoOperation {

	protected CaoMonitor monitor;
	protected CaoOperation nextOperation = null;

	public abstract void initialize() throws CaoException;

	public abstract void execute() throws CaoException;

	public abstract void dispose() throws CaoException;

	public void setMonitor(CaoMonitor monitor) {
		this.monitor=monitor;
	}

	public CaoOperation getNextOperation() {
		return nextOperation;
	}

}
