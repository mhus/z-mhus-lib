package de.mhus.lib.core.strategy;

import de.mhus.lib.core.MLog;

public abstract class AbstractOperation extends MLog implements Operation {

	private Object owner;

	@Override
	public final OperationResult doExecute(TaskContext context) throws Exception {
		if (!canExecute(context)) return new NotSuccessful(this, "can't execute");
		return doExecute2(context);
	}
	
	protected abstract OperationResult doExecute2(TaskContext context) throws Exception;

	@Override
	public boolean isBusy() {
		synchronized (this) {
			return owner != null;
		}
	}

	@Override
	public boolean setBusy(Object owner) {
		synchronized (this) {
			if (this.owner != null) return false;
			this.owner = owner;
		}
		return true;
	}

	@Override
	public boolean releaseBusy(Object owner) {
		synchronized (this) {
			if (this.owner == null) return true;
//			if (!this.owner.equals(owner)) return false;
			if (this.owner != owner) return false;
			this.owner = null;
		}
		return true;
	}

}
