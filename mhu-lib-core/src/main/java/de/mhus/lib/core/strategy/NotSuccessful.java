package de.mhus.lib.core.strategy;

import de.mhus.lib.core.util.Rfc1738;

/**
 * <p>NotSuccessful class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class NotSuccessful extends OperationResult {

	/**
	 * <p>Constructor for NotSuccessful.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param rc a long.
	 */
	public NotSuccessful(String path, String msg, long rc) {
		setSuccessful(false);
		setMsg(Rfc1738.encode(msg));
		setOperationPath(path);
		setReturnCode(rc);
	}
	
	/**
	 * <p>Constructor for NotSuccessful.</p>
	 *
	 * @param operation a {@link de.mhus.lib.core.strategy.Operation} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param rc a long.
	 */
	public NotSuccessful(Operation operation, String msg, long rc) {
		setSuccessful(false);
		setMsg(Rfc1738.encode(msg));
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setTitle(operation.getDescription().getTitle());
		}
	}
	
	/**
	 * <p>Constructor for NotSuccessful.</p>
	 *
	 * @param operation a {@link de.mhus.lib.core.strategy.Operation} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param title a {@link java.lang.String} object.
	 * @param rc a long.
	 * @since 3.3.0
	 */
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
