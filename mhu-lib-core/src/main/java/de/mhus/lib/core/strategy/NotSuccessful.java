package de.mhus.lib.core.strategy;

import de.mhus.lib.core.util.Rfc1738;

public class NotSuccessful extends OperationResult {

	public NotSuccessful(String path, String msg, long rc) {
		setSuccessful(false);
		setMsg(Rfc1738.encode(msg));
		setOperationPath(path);
		setReturnCode(rc);
	}
	
	public NotSuccessful(Operation operation, String msg, long rc) {
		setSuccessful(false);
		setMsg(Rfc1738.encode(msg));
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setTitle(operation.getDescription().getTitle());
		}
	}
	
	public NotSuccessful(Operation operation, String msg, String title, long rc) {
		setSuccessful(false);
		setMsg(Rfc1738.encode(msg) + "&" + Rfc1738.encode(title));
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setTitle(operation.getDescription().getTitle());
		}
	}
	
}
