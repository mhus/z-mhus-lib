package de.mhus.lib.core.strategy;

import de.mhus.lib.core.util.MUri;

public class ProfessionalError extends OperationResult {

	public ProfessionalError(String path, String msg, long rc) {
		setSuccessful(true);
		setMsg(MUri.implodeKeyValues("m",msg));
		setOperationPath(path);
		if (rc >= 0) rc = INTERNAL_ERROR;
		setReturnCode(rc);
	}
	
	public ProfessionalError(Operation operation, String msg, long rc) {
		setSuccessful(false);
		setMsg(MUri.implodeKeyValues("m",msg));
		if (rc >= 0) rc = INTERNAL_ERROR;
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setCaption(operation.getDescription().getCaption());
		}
	}

	public ProfessionalError(Operation operation, String msg, String caption, long rc) {
		setSuccessful(false);
		setMsg(MUri.implodeKeyValues("m",msg, "c", caption));
		if (rc >= 0) rc = INTERNAL_ERROR;
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setCaption(operation.getDescription().getCaption());
		}
	}

}
