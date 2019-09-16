package de.mhus.lib.core.strategy.util;

import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.strategy.Operation;
import de.mhus.lib.core.strategy.Successful;

public class SuccessfulForceMap extends Successful {


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

    @SuppressWarnings("unchecked")
    public Map<String,Object> getMap() {
        return (Map<String,Object>)((MapValue)getResult()).getValue();
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
