package de.mhus.lib.core.strategy;

public class Successful extends OperationResult {

	public Successful(Operation operation, String msg) {
		this(operation, msg, 0, null);
	}
	
	public Successful(Operation operation, String msg, Object result) {
		this(operation, msg, 0, result);
	}
	
	public Successful(Operation operation, String msg, long rc, Object result) {
		setOperationPath(operation.getDescription().getPath());
		setTitle(operation.getDescription().getTitle());
		setMsg(msg);
		setResult(result);
		setReturnCode(rc);
		setSuccessful(true);
	}
	
}
