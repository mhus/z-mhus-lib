package de.mhus.lib.core.strategy;

import de.mhus.lib.core.MSystem;
import de.mhus.lib.core.pojo.ObjectWrapperPojo;

public class OperationResult {

	private String operationPath;
	private String title;
	private String msg;
	private ObjectWrapperPojo<Object> result; // technical result
	private boolean successful;
	
	private OperationDescription nextOperation;
	
	public OperationResult() {
		
	}
	public OperationResult(OperationDescription description) {
		if (description != null) {
			setOperationPath(description.getGroup() + "/" + description.getId());
			setTitle(description.getTitle());
		}
	}
	public String getOperationPath() {
		return operationPath;
	}
	public void setOperationPath(String operationPath) {
		this.operationPath = operationPath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public ObjectWrapperPojo<Object> getResult() {
		return result;
	}
	public void setResult(ObjectWrapperPojo<Object> result) {
		this.result = result;
	}
	
	public String toString() {
		return MSystem.toString(this, operationPath, successful, msg, nextOperation ); // result ?
	}

}
