package de.mhus.lib.core.strategy;

public class ProfessionalError extends OperationResult {

	public ProfessionalError(String path, String msg, long rc) {
		setSuccessful(true);
		setMsg(msg);
		setOperationPath(path);
		if (rc >= 0) rc = INTERNAL_ERROR;
		setReturnCode(rc);
	}
	
	public ProfessionalError(Operation operation, String msg, long rc) {
		setSuccessful(false);
		setMsg(msg);
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setTitle(operation.getDescription().getTitle());
		}
	}

}
