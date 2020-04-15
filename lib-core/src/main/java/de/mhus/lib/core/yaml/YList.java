package de.mhus.lib.core.yaml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.util.EmptyList;

public class YList extends YElement implements Iterable<YElement> {

	private List<Object> list;

	@SuppressWarnings("unchecked")
	public YList(Object obj) {
	    super(obj);
		list = (List<Object>)obj;
	}
	
	public int size() {
	    if (list == null) return 0;
		return list.size();
	}
	
	public String getString(int index) {
        if (list == null || index >= list.size()) return null;
		Object ret = list.get(index);
		if (ret == null) return null;
		if (ret instanceof String) return (String) ret;
		return String.valueOf(ret);
	}
	
    public YMap getMap(int index) {
        if (list == null || index >= list.size()) return null;
        Object ret = list.get(index);
        if (ret == null) return null;
        return new YMap(ret);
    }
    
	public List<String> toStringList() {
        if (list == null) return new EmptyList<>();
		LinkedList<String> ret = new LinkedList<>();
		for (int i = 0; i < list.size(); i++)
			ret.add(getString(i));
		return ret;
	}

    public List<YMap> toMapList() {
        if (list == null) return new EmptyList<>();
        LinkedList<YMap> ret = new LinkedList<>();
        for (int i = 0; i < list.size(); i++)
            ret.add(getMap(i));
        return ret;
    }

    @Override
    public Iterator<YElement> iterator() {
        ArrayList<YElement> out = new ArrayList<>(size());
        list.forEach(i -> out.add(new YElement(i)));
        return out.iterator();
    }
    
    public YElement getElement(int index) {
        if (list == null || index >= list.size()) return null;
        Object ret = list.get(index);
        return new YElement(ret);
    }

    public int getInteger(int index) {
        return getInteger(index, 0);
    }
    
    public int getInteger(int index, int def) {
        if (list == null || index >= list.size()) return def;
        Object ret = list.get(index);
        if (ret == null) return def;
        if (ret instanceof Number) return ((Number) ret).intValue();
        return MCast.toint(ret, def);
    }

    public boolean isInteger(int index) {
        if (list == null || index >= list.size()) return false;
        Object val = list.get(index);
        if (val == null) return false;
        return val instanceof Number;
    }

    public void add(YElement item) {
        if (item == null || item.getObject() == null) return;
        list.add(item.getObject());
    }

}
