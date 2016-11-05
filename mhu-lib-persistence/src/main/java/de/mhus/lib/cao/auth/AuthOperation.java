package de.mhus.lib.cao.auth;

import de.mhus.lib.cao.CaoMonitor;
import de.mhus.lib.cao.CaoOperation;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.OperationDescription;
import de.mhus.lib.core.strategy.OperationResult;
import de.mhus.lib.core.strategy.TaskContext;

public class AuthOperation extends CaoOperation {

	private CaoOperation instance;
	@SuppressWarnings("unused")

	public AuthOperation(AuthConnection con, CaoOperation instance) {
		super(con);
		this.instance = instance;
	}
	@Override
	public void setMonitor(CaoMonitor monitor) {
		instance.setMonitor(monitor);
	}
	@Override
	public OperationResult doExecute(IProperties properties) {
		return instance.doExecute(properties);
	}
	@Override
	public boolean hasAccess() {
		return instance.hasAccess();
	}
	@Override
	public boolean canExecute(TaskContext context) {
		return instance.canExecute(context);
	}
	@Override
	public OperationDescription getDescription() {
		return instance.getDescription();
	}
	@Override
	public boolean isBusy() {
		return instance.isBusy();
	}
	@Override
	public boolean setBusy(Object owner) {
		return instance.setBusy(owner);
	}
	@Override
	public boolean releaseBusy(Object owner) {
		return instance.releaseBusy(owner);
	}

}
