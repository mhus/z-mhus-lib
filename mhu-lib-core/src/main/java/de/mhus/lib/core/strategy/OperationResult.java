package de.mhus.lib.core.strategy;

import de.mhus.lib.core.MSystem;

/**
 * <p>OperationResult class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class OperationResult {

	/** Constant <code>OK=0</code> */
	public static final long OK = 0;
	/** Constant <code>EMPTY=-10</code> */
	public static final long EMPTY = -10;
	/** Constant <code>BUSY=-11</code> */
	public static final long BUSY = -11;
	/** Constant <code>NOT_EXECUTABLE=-12</code> */
	public static final long NOT_EXECUTABLE = -12;
	/** Constant <code>SYNTAX_ERROR=-13</code> */
	public static final long SYNTAX_ERROR = -13;
	/** Constant <code>USAGE=-14</code> */
	public static final long USAGE = -14;

	/** Constant <code>INTERNAL_ERROR=-500</code> */
	public static final long INTERNAL_ERROR = -500;
	/** Constant <code>ACCESS_DENIED=-401</code> */
	public static final long ACCESS_DENIED = -401;
	/** Constant <code>NOT_FOUND=-404</code> */
	public static final long NOT_FOUND = -404;
	/** Constant <code>NOT_SUPPORTED=-505</code> */
	public static final long NOT_SUPPORTED = -505;
	/** Constant <code>WRONG_STATUS=-506</code> */
	public static final long WRONG_STATUS = -506;

	private String operationPath;
	private String title;
	private String msg;
	private Object result; // technical result
	private boolean successful;
	private long returnCode = 0;
	
	private OperationDescription nextOperation;
	
	/**
	 * <p>Constructor for OperationResult.</p>
	 */
	public OperationResult() {
		
	}
	/**
	 * <p>Constructor for OperationResult.</p>
	 *
	 * @param description a {@link de.mhus.lib.core.strategy.OperationDescription} object.
	 */
	public OperationResult(OperationDescription description) {
		if (description != null) {
			setOperationPath(description.getGroup() + "/" + description.getId());
			setTitle(description.getTitle());
		}
	}
	/**
	 * <p>Getter for the field <code>operationPath</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getOperationPath() {
		return operationPath;
	}
	/**
	 * <p>Setter for the field <code>operationPath</code>.</p>
	 *
	 * @param operationPath a {@link java.lang.String} object.
	 */
	public void setOperationPath(String operationPath) {
		this.operationPath = operationPath;
	}
	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * <p>Setter for the field <code>title</code>.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * <p>Getter for the field <code>msg</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * <p>Setter for the field <code>msg</code>.</p>
	 *
	 * @param msg a {@link java.lang.String} object.
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	/**
	 * <p>isSuccessful.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSuccessful() {
		return successful;
	}
	/**
	 * <p>Setter for the field <code>successful</code>.</p>
	 *
	 * @param successful a boolean.
	 */
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	/**
	 * <p>Getter for the field <code>nextOperation</code>.</p>
	 *
	 * @return a {@link de.mhus.lib.core.strategy.OperationDescription} object.
	 */
	public OperationDescription getNextOperation() {
		return nextOperation;
	}
	/**
	 * <p>Setter for the field <code>nextOperation</code>.</p>
	 *
	 * @param nextOperation a {@link de.mhus.lib.core.strategy.OperationDescription} object.
	 */
	public void setNextOperation(OperationDescription nextOperation) {
		this.nextOperation = nextOperation;
	}
	/**
	 * <p>Getter for the field <code>result</code>.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getResult() {
		return result;
	}
	/**
	 * <p>Setter for the field <code>result</code>.</p>
	 *
	 * @param result a {@link java.lang.Object} object.
	 */
	public void setResult(Object result) {
		this.result = result;
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return MSystem.toString(this, operationPath, successful, msg, nextOperation ); // result ?
	}
	/**
	 * <p>Getter for the field <code>returnCode</code>.</p>
	 *
	 * @return a long.
	 */
	public long getReturnCode() {
		return returnCode;
	}
	/**
	 * <p>Setter for the field <code>returnCode</code>.</p>
	 *
	 * @param returnCode a long.
	 */
	public void setReturnCode(long returnCode) {
		this.returnCode = returnCode;
	}

}
