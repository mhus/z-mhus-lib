package de.mhus.lib.core.strategy;

public class NotSuccessful extends OperationResult {

	public NotSuccessful(String path, String msg, long rc) {
		setSuccessful(false);
		setMsg(msg);
		setOperationPath(path);
		setReturnCode(rc);
	}
	
	public NotSuccessful(Operation operation, String msg, long rc) {
		setSuccessful(false);
		setMsg(msg);
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setTitle(operation.getDescription().getTitle());
		}
	}

}
