package de.mhus.lib.core.strategy;

public class NotSuccessful extends OperationResult {

	public NotSuccessful(Operation oper, String msg) {
		setSuccessful(false);
		setMsg(msg);
		if (oper != null && oper.getDescription() != null)
			setOperationPath(oper.getDescription().getPath());
	}

}
