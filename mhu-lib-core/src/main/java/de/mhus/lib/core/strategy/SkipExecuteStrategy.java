package de.mhus.lib.core.strategy;

public class SkipExecuteStrategy extends ExecuteStrategy {

	private Operation executable;
	
	@Override
	protected OperationResult doExecute2(TaskContext context) throws Exception {
		synchronized (this) {
			if (executable == null) return new NotSuccessful(this, "executable not found", OperationResult.EMPTY);
			if (executable.isBusy()) return new NotSuccessful(this, "skip", OperationResult.BUSY);

			executable.setBusy(this);
			OperationResult out = executable.doExecute(context);
			executable.releaseBusy(this);
			return out;
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

	@Override
	public OperationDescription getDescription() {
		if (executable == null) return null;
		return executable.getDescription();
	}

	@Override
	public boolean canExecute(TaskContext context) {
		if (executable == null) return false;
		return executable.canExecute(context);
	}
	
}
