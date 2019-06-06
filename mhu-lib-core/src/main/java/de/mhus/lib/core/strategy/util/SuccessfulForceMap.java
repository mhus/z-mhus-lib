package de.mhus.lib.core.strategy.util;

import java.util.Map;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.SuccessfulMap;

public class SuccessfulForceMap extends SuccessfulMap {


    public SuccessfulForceMap(Operation operation, String msg) {
        super(operation, msg);
    }

    public SuccessfulForceMap(String path, String msg, long rc, String... keyValues) {
        super(path, msg, rc, keyValues);
    }

    public SuccessfulForceMap(String path, String msg, long rc) {
        super(path, msg, rc);
    }

    @Override
    public void setResult(Object result) {
        super.setResult(new MapValue((Map<?,?>)result));
    }

    @Override
    public IProperties getMap() {
        return (IProperties)((MapValue)getResult()).getValue();
    }

}
