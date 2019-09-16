package de.mhus.lib.core.strategy.util;

import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.lang.SerializedValue;
import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.Successful;

// TODO use de.mhus.lib.core.strategy.SuccessfulSerializedMap in mhu-lib 3.6.2
public class SuccessfulSerializedMap extends Successful {

    public SuccessfulSerializedMap(Operation operation, String msg) {
        super(operation, msg);
        setResult(new SerializedValue(new MProperties()));
    }

    public SuccessfulSerializedMap(String path, String msg, long rc) {
        super(path, msg, rc, new MProperties());
    }

    public SuccessfulSerializedMap(String path, String msg, long rc, String... keyValues) {
        super(path, msg, rc, keyValues);
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> getMap() {
        return (Map<String,Object>)((SerializedValue)getResult()).getValue();
    }

    public void put(String key, Object value) {
       getMap().put(key, value);
    }
    
    public Object get(String key) {
        return getMap().get(key);
    }
    
    public void remove(String key) {
        getMap().remove(key);
    }

    public Set<String> keySet() {
        return getMap().keySet();
    }

    public int size() {
        return getMap().size();
    }
    
}
