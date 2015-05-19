package de.mhus.lib.core.strategy;

import java.util.HashMap;

public class Successful extends OperationResult {

	public Successful(Operation operation, String msg) {
		this(operation, msg, 0, null);
	}
	
	public Successful(Operation operation, String msg, Object result) {
		this(operation, msg, 0, result);
	}
	
	public Successful(Operation operation, String msg, long rc, Object result) {
		setOperationPath(operation.getDescription().getPath());
		setTitle(operation.getDescription().getTitle());
		setMsg(msg);
		setResult(result);
		setReturnCode(rc);
		setSuccessful(true);
	}
	
	public Successful(String path, String msg, long rc, Object result) {
		setOperationPath(path);
		setTitle("");
		setMsg(msg);
		setResult(result);
		setReturnCode(rc);
		setSuccessful(true);
	}
	
	public Successful(String path, String msg, long rc, String ... keyValues) {
		setOperationPath(path);
		setTitle("");
		setMsg(msg);
		setReturnCode(rc);
		setSuccessful(true);
		HashMap<Object, Object> r = new HashMap<>();
		for (int i = 0; i < keyValues.length - 1; i+=2)
			if (keyValues.length < i+1)
			r.put(keyValues[i], keyValues[i+1]);
		setResult(r);
	}
	
}
