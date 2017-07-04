package de.mhus.lib.core.strategy;

import java.util.HashMap;

import de.mhus.lib.core.util.MUri;

public class ProfessionalError extends OperationResult {

	public ProfessionalError(String path, String msg, long rc) {
		setSuccessful(false);
		setMsg(MUri.implodeKeyValues("m",msg));
		setOperationPath(path);
		if (rc >= 0) rc = INTERNAL_ERROR;
		setReturnCode(rc);
//		setResult(new HashMap<>());
//		((HashMap)getResult()).put("successful", false);
	}
	
	public ProfessionalError(String path, String msg, String caption, long rc) {
		setSuccessful(false);
		setMsg(MUri.implodeKeyValues("m",msg, "c", caption));
		setOperationPath(path);
		if (rc >= 0) rc = INTERNAL_ERROR;
		setReturnCode(rc);
//		setResult(new HashMap<>());
//		((HashMap)getResult()).put("successful", false);
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
//		setResult(new HashMap<>());
//		((HashMap)getResult()).put("successful", false);
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
//		setResult(new HashMap<>());
//		((HashMap)getResult()).put("successful", false);
	}

}
