package de.mhus.lib.core.strategy;


/**
 * <p>SynchronizedExecuteStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class SynchronizedExecuteStrategy extends ExecuteStrategy {

	private Operation executable;
	
	/** {@inheritDoc} */
	@Override
	protected OperationResult doExecute2(TaskContext context) throws Exception {
		synchronized (this) {
			if (executable == null) return new NotSuccessful(this, "executable not found", OperationResult.EMPTY);
			executable.setBusy(this);
			OperationResult out = executable.doExecute(context);
			executable.releaseBusy(this);
			return out;
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
