package de.mhus.lib.core.strategy;

import java.util.HashMap;

/**
 * <p>Successful class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class Successful extends OperationResult {

	/**
	 * <p>Constructor for Successful.</p>
	 *
	 * @param operation a {@link de.mhus.lib.core.strategy.Operation} object.
	 */
	public Successful(Operation operation) {
		this(operation, "", 0, null);
	}
	
	/**
	 * <p>Constructor for Successful.</p>
	 *
	 * @param operation a {@link de.mhus.lib.core.strategy.Operation} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param result a {@link java.lang.Object} object.
	 */
	public Successful(Operation operation, String msg, Object result) {
		this(operation, msg, 0, result);
	}
	
	/**
	 * <p>Constructor for Successful.</p>
	 *
	 * @param operation a {@link de.mhus.lib.core.strategy.Operation} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param rc a long.
	 * @param result a {@link java.lang.Object} object.
	 */
	public Successful(Operation operation, String msg, long rc, Object result) {
		setOperationPath(operation.getDescription().getPath());
		setTitle(operation.getDescription().getTitle());
		setMsg(msg);
		setResult(result);
		setReturnCode(rc);
		setSuccessful(true);
	}
	
	/**
	 * <p>Constructor for Successful.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param rc a long.
	 * @param result a {@link java.lang.Object} object.
	 */
	public Successful(String path, String msg, long rc, Object result) {
		setOperationPath(path);
		setTitle("");
		setMsg(msg);
		setResult(result);
		setReturnCode(rc);
		setSuccessful(true);
	}
	
	/**
	 * <p>Constructor for Successful.</p>
	 *
	 * @param operation a {@link de.mhus.lib.core.strategy.Operation} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param keyValues a {@link java.lang.String} object.
	 */
	public Successful(Operation operation, String msg, String ... keyValues) {
		this(operation.getDescription().getPath(), msg, 0, keyValues);
	}
	
	/**
	 * <p>Constructor for Successful.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param msg a {@link java.lang.String} object.
	 * @param rc a long.
	 * @param keyValues a {@link java.lang.String} object.
	 */
	public Successful(String path, String msg, long rc, String ... keyValues) {
		setOperationPath(path);
		setTitle("");
		setMsg(msg);
		setReturnCode(rc);
		setSuccessful(true);
		HashMap<Object, Object> r = new HashMap<>();
		for (int i = 0; i < keyValues.length - 1; i+=2)
			if (keyValues.length > i+1)
			r.put(keyValues[i], keyValues[i+1]);
		setResult(r);
	}
	
}
