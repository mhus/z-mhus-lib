package de.mhus.lib.core.strategy.util;

import java.io.Serializable;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.lang.SerializedValue;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.SuccessfulMap;

public class SuccessfulSerializedMap extends SuccessfulMap {


    public SuccessfulSerializedMap(Operation operation, String msg) {
        super(operation, msg);
    }

    public SuccessfulSerializedMap(String path, String msg, long rc, String... keyValues) {
        super(path, msg, rc, keyValues);
    }

    public SuccessfulSerializedMap(String path, String msg, long rc) {
        super(path, msg, rc);
    }

    @Override
    public void setResult(Object result) {
        super.setResult(new SerializedValue((Serializable)result));
    }

    @Override
    public IProperties getMap() {
        return (IProperties)((SerializedValue)getResult()).getValue();
    }

}
