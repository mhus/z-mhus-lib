package de.mhus.lib.core.strategy;

import de.mhus.lib.core.util.MUri;

public class NotSuccessful extends OperationResult {

	public NotSuccessful(String path, String msg, long rc) {
		setSuccessful(false);
		setMsg(MUri.implodeKeyValues("m",msg));
		setOperationPath(path);
		setReturnCode(rc);
	}
	
	public NotSuccessful(Operation operation, String msg, long rc) {
		setSuccessful(false);
		setMsg(MUri.implodeKeyValues("m",msg));
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setCaption(operation.getDescription().getCaption());
		}
	}
	
	public NotSuccessful(Operation operation, String msg, String caption, long rc) {
		setSuccessful(false);
		setMsg(MUri.implodeKeyValues("m",msg, "c", caption));
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setCaption(operation.getDescription().getCaption());
		}
	}
	
}
