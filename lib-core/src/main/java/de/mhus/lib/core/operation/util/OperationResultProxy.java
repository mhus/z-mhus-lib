package de.mhus.lib.core.operation.util;

import de.mhus.lib.core.operation.OperationResult;

public class OperationResultProxy extends OperationResult {

    public OperationResultProxy(OperationResult instance) {
        setSuccessful(instance.isSuccessful());
        setReturnCode(instance.getReturnCode());
        setMsg(instance.getMsg());
        setResult(instance.getResult());
        setNextOperation(instance.getNextOperation());
        setOperationPath(instance.getOperationPath());
    }
}
