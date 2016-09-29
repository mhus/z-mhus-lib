package de.mhus.lib.core.strategy;

/**
 * <p>ProfessionalError class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.3.0
 */
public class ProfessionalError extends OperationResult {

	/**
	 * <p>Constructor for ProfessionalError.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param rc a long.
	 */
	public ProfessionalError(String path, String msg, long rc) {
		setSuccessful(true);
		setMsg(msg);
		setOperationPath(path);
		if (rc >= 0) rc = INTERNAL_ERROR;
		setReturnCode(rc);
	}
	
	/**
	 * <p>Constructor for ProfessionalError.</p>
	 *
	 * @param operation a {@link de.mhus.lib.core.strategy.Operation} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param rc a long.
	 */
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
