package de.mhus.lib.core.strategy;

/**
 * <p>NotSuccessful class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
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
		setMsg(msg);
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
		setMsg(msg);
		setReturnCode(rc);
		if (operation != null && operation.getDescription() != null) {
			setOperationPath(operation.getDescription().getPath());
			setTitle(operation.getDescription().getTitle());
		}
	}

}
