package de.mhus.lib.core.yaml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.util.EmptyList;

public class YList extends YElement implements Iterable<YElement> {

    public YList(Object obj) {
        super(obj);
    }

    @SuppressWarnings("unchecked")
    public int size() {
        if (getObject() == null) return 0;
        return ((List<Object>) getObject()).size();
    }

    @SuppressWarnings("unchecked")
    public String getString(int index) {
        if (getObject() == null || index >= ((List<Object>) getObject()).size()) return null;
        Object ret = ((List<Object>) getObject()).get(index);
        if (ret == null) return null;
        if (ret instanceof String) return (String) ret;
        return String.valueOf(ret);
    }

    @SuppressWarnings("unchecked")
    public YMap getMap(int index) {
        if (getObject() == null || index >= ((List<Object>) getObject()).size()) return null;
        Object ret = ((List<Object>) getObject()).get(index);
        if (ret == null) return null;
        return new YMap(ret);
    }

    @SuppressWarnings("unchecked")
    public List<String> toStringList() {
        if (getObject() == null) return new EmptyList<>();
        LinkedList<String> ret = new LinkedList<>();
        for (int i = 0; i < ((List<Object>) getObject()).size(); i++) ret.add(getString(i));
        return ret;
    }

    @SuppressWarnings("unchecked")
    public List<YMap> toMapList() {
        if (getObject() == null) return new EmptyList<>();
        LinkedList<YMap> ret = new LinkedList<>();
        for (int i = 0; i < ((List<Object>) getObject()).size(); i++) ret.add(getMap(i));
        return ret;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<YElement> iterator() {
        ArrayList<YElement> out = new ArrayList<>(size());
        ((List<Object>) getObject()).forEach(i -> out.add(new YElement(i)));
        return out.iterator();
    }

    @SuppressWarnings("unchecked")
    public YElement getElement(int index) {
        if (getObject() == null || index >= ((List<Object>) getObject()).size()) return null;
        Object ret = ((List<Object>) getObject()).get(index);
        return new YElement(ret);
    }

    public int getInteger(int index) {
        return getInteger(index, 0);
    }

    @SuppressWarnings("unchecked")
    public int getInteger(int index, int def) {
        if (getObject() == null || index >= ((List<Object>) getObject()).size()) return def;
        Object ret = ((List<Object>) getObject()).get(index);
        if (ret == null) return def;
        if (ret instanceof Number) return ((Number) ret).intValue();
        return MCast.toint(ret, def);
    }

    @SuppressWarnings("unchecked")
    public boolean isInteger(int index) {
        if (getObject() == null || index >= ((List<Object>) getObject()).size()) return false;
        Object val = ((List<Object>) getObject()).get(index);
        if (val == null) return false;
        return val instanceof Number;
    }

    @SuppressWarnings("unchecked")
    public void add(YElement item) {
        if (item == null || item.getObject() == null) return;
        ((List<Object>) getObject()).add(item.getObject());
    }

    @Override
    public String toString() {
        return getObject() == null ? null : getObject().toString();
    }
}
