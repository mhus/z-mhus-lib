package de.mhus.lib.core.yaml;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.util.EmptySet;

public class YMap extends YElement {

	private Map<String, Object> map;

	@SuppressWarnings("unchecked")
	public YMap(Object obj) {
	    super(obj);
		map = (Map<String, Object>)obj;
	}

	public Set<String> getKeys() {
	    if (map == null) return new EmptySet<>();
	    return map.keySet();
	}
	public YMap getMap(String key) {
	    if (map == null) return null;
	    Object ret = map.get(key);
	    if (ret == null) return null;
		return new YMap(ret);
	}
	
	public YList getList(String key) {
        if (map == null) return null;
        Object ret = map.get(key);
        if (ret == null) return null;
		return new YList(ret);
	}
	
    public String getString(String key) {
        return getString(key, null);
    }
    
	public String getString(String key, String def) {
        if (map == null) return def;
		Object val = map.get(key);
		if (val == null) return def;
		if (val instanceof String) return (String) val;
		return String.valueOf(val);
	}
	
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
    
    public boolean getBoolean(String key, boolean def) {
        if (map == null) return def;
        Object val = map.get(key);
        if (val == null) return def;
        if (val instanceof Boolean) return (Boolean) val;
        return MCast.toboolean(val, def);
    }

    public boolean isEmpty() {
        return map == null || map.size() == 0;
    }

    public boolean isString(String key) {
        Object val = map.get(key);
        return val instanceof String;
    }
    
    public boolean isList(String key) {
        Object val = map.get(key);
        return val instanceof List;
    }

    public boolean isMap(String key) {
        Object val = map.get(key);
        return val instanceof Map;
    }

    public Object getObject(String key) {
        return map.get(key);
    }
    
    public YElement getElement(String key) {
        Object val = map.get(key);
        if (val == null) return null;
        return new YElement(val);
    }
    
    public int getInteger(String key) {
        return getInteger(key, 0);
    }
    
    public int getInteger(String key, int def) {
        if (map == null) return def;
        Object val = map.get(key);
        if (val == null) return def;
        if (val instanceof Number) return ((Number) val).intValue();
        return MCast.toint(val, def);
    }
    
    public boolean isInteger(String key) {
        if (map == null) return false;
        Object val = map.get(key);
        if (val == null) return false;
        return val instanceof Number;
    }

    public void put(String key, YElement elem) {
        if (elem == null || elem.getObject() == null)
            map.remove(key);
        else
            map.put(key, elem.getObject());
    }

}
