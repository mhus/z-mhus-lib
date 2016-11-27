package de.mhus.lib.core.strategy;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MSystem;

public class OperationResult {

	public static final long OK = 0;
	public static final long EMPTY = -10;
	public static final long BUSY = -11;
	public static final long NOT_EXECUTABLE = -12;
	public static final long SYNTAX_ERROR = -13;
	public static final long USAGE = -14;

	public static final long INTERNAL_ERROR = -500;
	public static final long ACCESS_DENIED = -401;
	public static final long NOT_FOUND = -404;
	public static final long NOT_SUPPORTED = -505;
	public static final long WRONG_STATUS = -506;

	private String operationPath;
	private String caption;
	private String msg;
	private Object result; // technical result
	private boolean successful;
	private long returnCode = 0;
	
	private OperationDescription nextOperation;
	
	public OperationResult() {
		
	}
	public OperationResult(OperationDescription description) {
		if (description != null) {
			setOperationPath(description.getGroup() + "/" + description.getId());
			setCaption(description.getCaption());
		}
	}
	public String getOperationPath() {
		return operationPath;
	}
	public void setOperationPath(String operationPath) {
		this.operationPath = operationPath;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String title) {
		this.caption = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isSuccessful() {
		return successful;
	}
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}
	public OperationDescription getNextOperation() {
		return nextOperation;
	}
	public void setNextOperation(OperationDescription nextOperation) {
		this.nextOperation = nextOperation;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
	public boolean isResult(Class<?> clazz) {
		return result != null && clazz.isInstance(result);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getResultAs(Class<T> clazz) {
		// TODO try to cast or convert
		return (T)result;
	}

	@SuppressWarnings("unchecked")
	public <T> T getResultAs() {
		return (T)result;
	}

	@Override
	public String toString() {
		return MSystem.toString(this, operationPath, successful, msg, nextOperation ); // result ?
	}
	public long getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(long returnCode) {
		this.returnCode = returnCode;
	}

}
