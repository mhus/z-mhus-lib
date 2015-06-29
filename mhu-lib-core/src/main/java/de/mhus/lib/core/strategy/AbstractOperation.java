package de.mhus.lib.core.strategy;

import de.mhus.lib.core.MLog;

public abstract class AbstractOperation extends MLog implements Operation {

	private Object owner;

	@Override
	public final OperationResult doExecute(TaskContext context) throws Exception {
		log().d("execute",context.getParameters());
		if (!hasAccess()) {
			log().d("access denied",context,context.getErrorMessage());
			return new NotSuccessful(this, "access denied", OperationResult.ACCESS_DENIED);
		}
		if (!canExecute(context)) {
			log().d("execution denied",context.getErrorMessage());
			return new NotSuccessful(this, context.getErrorMessage() != null ? context.getErrorMessage() : "can't execute", OperationResult.NOT_EXECUTABLE);
		}
		OperationResult ret = doExecute2(context);
		log().d("result",ret);
		return ret;
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
