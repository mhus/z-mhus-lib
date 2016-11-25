package de.mhus.lib.core.strategy;

import de.mhus.lib.core.MLog;
import de.mhus.lib.core.util.MNls;
import de.mhus.lib.core.util.MNlsProvider;
import de.mhus.lib.core.util.ParameterDefinition;
import de.mhus.lib.core.util.ParameterDefinitions;

public abstract class AbstractOperation extends MLog implements Operation, MNlsProvider {

	private Object owner;
	private OperationDescription description;
	private MNls nls;

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

	@Override
	public boolean canExecute(TaskContext context) {
		if (getDescription() == null) return true; // no definition, no check
		return validateParameters(getDescription().getParameterDefinitions(), context);
	}

	@Override
	public OperationDescription getDescription() {
		if (description == null)
			description = createDescription();
		return description;
	}

	/**
	 * Create and return a operation definition. The method
	 * is called only one time.
	 * 
	 * @return
	 */
	protected abstract OperationDescription createDescription();

	public static boolean validateParameters(ParameterDefinitions definitions, TaskContext context) {
		if (definitions == null) return true;
		for (ParameterDefinition def : definitions.values()) {
			Object v = context.getParameters().get(def.getName());
			if (def.isMandatory() && v == null) return false;
		}
		return true;
	}
	
	@Override
	public MNls getNls() {
		if (nls == null)
			nls = MNls.lookup(this);
		return nls;
	}

}
