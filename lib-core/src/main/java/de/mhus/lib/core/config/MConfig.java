package de.mhus.lib.core.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.parser.CompiledString;
import de.mhus.lib.core.util.EmptyList;
import de.mhus.lib.core.util.SingleList;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.errors.NotFoundException;

public class MConfig extends MProperties implements IConfig {

    public static final List<IConfig> EMPTY_LIST = new EmptyList<>();
    protected String name;
    protected IConfig parent;
    protected ConfigStringCompiler compiler;
    protected HashMap<String, CompiledString> compiledCache;

    public MConfig() {}

    public MConfig(String name, IConfig parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public boolean isObject(String key) {
        Object val = get(key);
        if (val == null) return false;
        return val instanceof IConfig;
    }

    @Override
    public IConfig getObject(String key) throws NotFoundException {
        Object val = get(key);
        if (val == null) throw new NotFoundException("value not found", key);
        if (val instanceof IConfig) return (IConfig) val;
        throw new NotFoundException("value is not an IConfig", key);
    }

    @Override
    public IConfig getObjectOrNull(String key) {
        Object val = get(key);
        if (val == null) return null;
        if (val instanceof IConfig) return (IConfig) val;
        return null;
    }

    @Override
    public boolean isArray(String key) {
        Object val = get(key);
        if (val == null) return false;
        return val instanceof ConfigList;
    }

    @Override
    public ConfigList getArray(String key) throws NotFoundException {
        Object val = get(key);
        if (val == null) throw new NotFoundException("value not found", key);
        if (val instanceof List) return (ConfigList) val;
        throw new NotFoundException("value is not a ConfigList", key);
    }

    @Override
    public ConfigList getArrayOrNull(String key) {
        Object val = get(key);
        if (val == null) return null;
        if (val instanceof ConfigList) return (ConfigList) val;
        return null;
    }

    @Override
    public ConfigList getArrayOrCreate(String key) {
        Object val = get(key);
        if (val == null) return createArray(key);
        if (val instanceof ConfigList) return (ConfigList) val;
        return createArray(key);
    }

    @Override
    public List<IConfig> getObjectList(String key) {
        Object val = get(key);
        if (val == null) return MConfig.EMPTY_LIST;
        // if (val == null) throw new NotFoundException("value not found",key);
        if (val instanceof IConfig) return new SingleList<IConfig>((IConfig) val);
        if (val instanceof ConfigList) return Collections.unmodifiableList((ConfigList) val);
        return EMPTY_LIST;
        // throw new NotFoundException("value is not a ConfigList or IConfig",key);
    }

    @Override
    public IConfig getObjectByPath(String path) {
        if (path == null) return null;
        if (path.equals("") || path.equals(".")) return this;
        while (path.startsWith("/")) path = path.substring(1);
        if (path.length() == 0) return this;
        int p = path.indexOf('/');
        if (p < 0) return getObjectOrNull(path);
        IConfig next = getObjectOrNull(path.substring(0, p));
        if (next == null) return null;
        return next.getObjectByPath(path.substring(p + 1));
    }

    @Override
    public String getExtracted(String key, String def) {
        return getExtracted(key, def, 0);
    }

    @Override
    public String getExtracted(String key) {
        return getExtracted(key, null);
    }

    @Override
    public ConfigList getList(String key) {
        if (isArray(key)) return getArrayOrNull(key);
        if (isObject(key)) {
            ConfigList ret = new ConfigList(key, this);
            ret.add(getObjectOrNull(key));
            return ret;
        }
        if (containsKey(key)) {
            ConfigList ret = new ConfigList(key, this);
            MConfig obj = new MConfig(key, this);
            obj.put(NAMELESS_VALUE, get(key));
            ret.add(obj);
            return ret;
        }
        return new ConfigList(key, this);
    }

    protected String getExtracted(String key, String def, int level) {

        if (level > 10) return def;

        String value = getString(key, null);

        if (value == null) return def;
        if (value.indexOf('$') < 0) return value;

        synchronized (this) {
            if (compiler == null) {
                compiler = new ConfigStringCompiler(this);
                compiledCache = new HashMap<String, CompiledString>();
            }
            CompiledString cached = compiledCache.get(key);
            if (cached == null) {
                cached = compiler.compileString(value);
                compiledCache.put(key, cached);
            }
            try {
                return cached.execute(new ConfigStringCompiler.ConfigMap(level, this));
            } catch (MException e) {
                throw new MRuntimeException(key, e);
            }
        }
    }

    @Override
    public List<IConfig> getObjects() {
        ArrayList<IConfig> out = new ArrayList<>();
        for (Object val : values()) {
            if (val instanceof IConfig) out.add((IConfig) val);
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public void setObject(String key, IConfig object) {
        // TODO Warn if already bound ? - clone?
        ((MConfig) object).parent = this;
        ((MConfig) object).name = key;
        put(key, object);
    }

    @Override
    public void addObject(String key, IConfig object) {
        Object obj = get(key);
        if (obj != null) {
            if (obj instanceof ConfigList) {
                ((ConfigList) obj).add(object);
            } else if (obj instanceof IConfig) {
                LinkedList<Object> list = new LinkedList<>();
                list.add(obj);
                put(key, obj);
                list.add(object);
            } else {
                // overwrite non object and arrays
                ConfigList list = new ConfigList(key, this);
                put(key, list);
                list.add(object);
            }
        } else {
            setObject(key, object);
            return;
        }
    }

    @Override
    public IConfig createObject(String key) {
        IConfig obj = new MConfig();
        addObject(key, obj);
        return obj;
    }

    @Override
    public ConfigList createArray(String key) {
        ConfigList list = new ConfigList(key, this);
        put(key, list);
        return list;
    }

    /**
     * Return only the property keys without objects and arrays.
     *
     * @return The property keys
     */
    @Override
    public List<String> getPropertyKeys() {
        ArrayList<String> out = new ArrayList<>();
        for (Entry<String, Object> entry : entrySet()) {
            if (!(entry.getValue() instanceof IConfig) && !(entry.getValue() instanceof ConfigList))
                out.add(entry.getKey());
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IConfig getParent() {
        return parent;
    }

    @Override
    public List<String> getObjectKeys() {
        ArrayList<String> out = new ArrayList<>();
        for (Entry<String, Object> entry : entrySet()) {
            if (entry.getValue() instanceof IConfig) out.add(entry.getKey());
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public List<String> getArrayKeys() {
        ArrayList<String> out = new ArrayList<>();
        for (Entry<String, Object> entry : entrySet()) {
            if (entry.getValue() instanceof ConfigList) out.add(entry.getKey());
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public List<String> getObjectAndArrayKeys() {
        ArrayList<String> out = new ArrayList<>();
        for (Entry<String, Object> entry : entrySet()) {
            if (entry.getValue() instanceof IConfig || entry.getValue() instanceof ConfigList)
                out.add(entry.getKey());
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public synchronized String toString() {
        return name + super.toString();
    }
}
