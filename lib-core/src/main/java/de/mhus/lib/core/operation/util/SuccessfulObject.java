package de.mhus.lib.core.operation.util;

import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.OperationResult;

public class SuccessfulObject extends OperationResult {

    public SuccessfulObject(Operation operation, String msg, Object result) {
        this(operation.getDescription().getPath(), msg, 0, result);
        setCaption(operation.getDescription().getCaption());
    }

    public SuccessfulObject(Operation operation, String msg, int rc, Object result) {
        this(operation.getDescription().getPath(), msg, rc, result);
        setCaption(operation.getDescription().getCaption());
    }

    public SuccessfulObject(String path, String msg, Object result) {
        this(path, msg, 0, result);
    }

    public SuccessfulObject(String path, String msg, int rc, Object result) {
        setOperationPath(path);
        setResult(result);
        setMsg(msg);
        setReturnCode(rc);
        setSuccessful(true);
    }


}
