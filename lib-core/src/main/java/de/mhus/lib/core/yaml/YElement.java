package de.mhus.lib.core.yaml;

import java.util.List;
import java.util.Map;

public class YElement {

    private Object obj;

    public YElement(Object elem) {
        this.obj = elem;
    }

    public YMap getMap() {
        return new YMap(obj);
    }
    
    public YList getList() {
        return new YList(obj);
    }

//    public String getString() {
//        return getString(null);
//    }
//    
//    public String getString(String def) {
//        if (obj == null) return def;
//        if (obj instanceof String) return (String) obj;
//        return String.valueOf(obj);
//    }
//    
//    public boolean getBoolean() {
//        return getBoolean(false);
//    }
//    
//    public boolean getBoolean(boolean def) {
//        if (obj == null) return def;
//        if (obj instanceof Boolean) return (Boolean) obj;
//        return MCast.toboolean(obj, def);
//    }
//
//    public int getInteger() {
//        return getInteger(0);
//    }
//    
//    public int getInteger(int def) {
//        if (obj == null) return def;
//        if (obj instanceof Number) return ((Number) obj).intValue();
//        return MCast.toint(obj, def);
//    }

//    public boolean isInteger() {
//        if (obj == null) return false;
//        return obj instanceof Number;
//    }
//    
//    public boolean isString() {
//        return obj instanceof String;
//    }

    public boolean isList() {
        return obj instanceof List;
    }

    public boolean isMap() {
        return obj instanceof Map;
    }

    public Object getObject() {
        return obj;
    }

}
