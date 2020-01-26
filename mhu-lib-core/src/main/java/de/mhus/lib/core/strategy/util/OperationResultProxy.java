package de.mhus.lib.core.strategy.util;

import de.mhus.lib.core.strategy.OperationResult;

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
