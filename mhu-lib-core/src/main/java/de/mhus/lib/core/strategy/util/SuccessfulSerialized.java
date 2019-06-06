package de.mhus.lib.core.strategy.util;

import java.io.Serializable;

import de.mhus.lib.core.lang.SerializedValue;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.Successful;

public class SuccessfulSerialized extends Successful {

    public SuccessfulSerialized(Operation operation, String msg, long rc, Object result) {
        super(operation, msg, rc, result);
    }

    public SuccessfulSerialized(Operation operation, String msg, Object result) {
        super(operation, msg, result);
    }

    public SuccessfulSerialized(Operation operation, String msg, String... keyValues) {
        super(operation, msg, keyValues);
    }

    public SuccessfulSerialized(Operation operation) {
        super(operation);
    }

    public SuccessfulSerialized(String path, String msg, long rc, Object result) {
        super(path, msg, rc, result);
    }

    public SuccessfulSerialized(String path, String msg, long rc, String... keyValues) {
        super(path, msg, rc, keyValues);
    }

    public SuccessfulSerialized(String path) {
        super(path);
    }

    @Override
    public void setResult(Object result) {
        if (result != null && result instanceof Serializable && ! (result instanceof SerializedValue))
            result = new SerializedValue((Serializable) result);
        super.setResult(result);
    }

}
