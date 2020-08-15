package de.mhus.lib.core.config;

import java.util.Collection;
import java.util.LinkedList;

public class ConfigList extends LinkedList<IConfig> {

    private static final long serialVersionUID = 1L;
    private String name;
    private MConfig parent;

    public ConfigList(String name, MConfig parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public boolean addAll(int index, Collection<? extends IConfig> c) {
        c.forEach(
                i -> {
                    ((MConfig) i).name = name;
                    ((MConfig) i).parent = parent;
                });
        return super.addAll(index, c);
    }

    @Override
    public boolean add(IConfig e) {
        ((MConfig) e).name = name;
        ((MConfig) e).parent = parent;
        return super.add(e);
    }

    @Override
    public void addFirst(IConfig e) {
        ((MConfig) e).name = name;
        ((MConfig) e).parent = parent;
        super.addFirst(e);
    }

    @Override
    public void addLast(IConfig e) {
        ((MConfig) e).name = name;
        ((MConfig) e).parent = parent;
        super.addLast(e);
    }

    @Override
    public IConfig set(int index, IConfig e) {
        ((MConfig) e).name = name;
        ((MConfig) e).parent = parent;
        return super.set(index, e);
    }

    public IConfig createObject() {
        MConfig ret = new MConfig(name, parent);
        super.add(ret);
        return ret;
    }
}
