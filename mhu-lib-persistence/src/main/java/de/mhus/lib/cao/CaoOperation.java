package de.mhus.lib.cao;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.OperationDescription;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.TaskContext;

public abstract class CaoOperation implements Operation {

	protected CaoMonitor monitor;
	private Log log = null;
	protected CaoConnection con;
	
	public CaoOperation(CaoConnection con) {
		this.con = con;
	}
	
	public void setMonitor(CaoMonitor monitor) {
		this.monitor=monitor;
		this.log = monitor.log();
	}
	
	@Override
	public OperationResult doExecute(TaskContext context) throws Exception {
		setMonitor(monitor);
		return doExecute(context.getParameters());
	}

	public OperationResult doExecute() {
		return doExecute((IProperties)null);
	}

	public abstract OperationResult doExecute(IProperties properties);
	
	public Log log() {
		if (log == null) log = Log.getLog(getClass()); // disuse synchronize, in worst case I create two log instances
		return log;
	}

	@Override
	public boolean hasAccess() {
		return true;
	}
	@Override
	public boolean canExecute(TaskContext context) {
		return true;
	}
	@Override
	public OperationDescription getDescription() {
		return null;
	}
	@Override
	public boolean isBusy() {
		return false;
	}
	@Override
	public boolean setBusy(Object owner) {
		return false;
	}
	@Override
	public boolean releaseBusy(Object owner) {
		return true;
	}

	public CaoConnection getConnection() {
		return con;
	}
}
