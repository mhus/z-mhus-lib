package de.mhus.lib.core.strategy;

public class SkipExecuteStrategy extends ExecuteStrategy {

	private Operation executable;
	
	@Override
	public void doExecute(TaskContext context) throws Exception {
		synchronized (this) {
			if (executable == null) return;
			if (executable.isBusy()) return;

			executable.setBusy(this);
			executable.doExecute(context);
			executable.releaseBusy(this);
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
	
}
