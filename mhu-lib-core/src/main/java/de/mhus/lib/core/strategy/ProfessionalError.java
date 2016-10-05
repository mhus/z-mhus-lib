package de.mhus.lib.core.strategy;

import de.mhus.lib.core.util.Rfc1738;

public class ProfessionalError extends OperationResult {

	public ProfessionalError(String path, String msg, long rc) {
		setSuccessful(true);
		setMsg(Rfc1738.implodeKeyValues("m",msg));
		setOperationPath(path);
		if (rc >= 0) rc = INTERNAL_ERROR;
		setReturnCode(rc);
	}
	
	public ProfessionalError(Operation operation, String msg, long rc) {
		setSuccessful(false);
		setMsg(Rfc1738.implodeKeyValues("m",msg));
		if (rc >= 0) rc = INTERNAL_ERROR;
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setTitle(operation.getDescription().getTitle());
		}
	}

	public ProfessionalError(Operation operation, String msg, String caption, long rc) {
		setSuccessful(false);
		setMsg(Rfc1738.implodeKeyValues("m",msg, "c", caption));
		if (rc >= 0) rc = INTERNAL_ERROR;
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setTitle(operation.getDescription().getTitle());
		}
	}

}
