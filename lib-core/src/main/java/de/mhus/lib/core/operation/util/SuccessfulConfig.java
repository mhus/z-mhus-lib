package de.mhus.lib.core.operation.util;

import java.util.List;
import java.util.Set;

import de.mhus.lib.core.config.ConfigList;
import de.mhus.lib.core.config.ConfigSerializable;
import de.mhus.lib.core.config.IConfig;
import de.mhus.lib.core.config.MConfig;
import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.Successful;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.errors.NotFoundException;

public class SuccessfulConfig extends Successful {
    
    @SuppressWarnings("deprecation")
    public SuccessfulConfig(Operation operation, String msg) {
        super(operation, msg, 0, (String)null);
        setResult(new MConfig());
    }

    @SuppressWarnings("deprecation")
    public SuccessfulConfig(Operation operation, String msg, IConfig config) {
        super(operation, msg, 0, (String)null);
        setResult(config);
    }

    @SuppressWarnings("deprecation")
    public SuccessfulConfig(Operation operation, String msg, int rc, IConfig config) {
        super(operation, msg, rc, (String)null);
        setResult(config);
    }

    @SuppressWarnings("deprecation")
    public SuccessfulConfig(String path, String msg, IConfig config) {
        super(path, msg, 0, (String)null);
        setResult(config);
    }

    @SuppressWarnings("deprecation")
    public SuccessfulConfig(String path, String msg, int rc, IConfig config) {
        super(path, msg, rc, (String)null);
        setResult(config);
    }

    @SuppressWarnings("deprecation")
    public SuccessfulConfig(Operation operation, String msg, ConfigSerializable object) {
        super(operation, msg, 0, (String)null);
        MConfig cfg = new MConfig();
        if (object != null)
            try {   
                object.readSerializableConfig(cfg);
            } catch (Exception e) {
                throw new MRuntimeException(getOperationPath(), msg,e);
            }
        setResult(cfg);
    }

    @SuppressWarnings("deprecation")
    public SuccessfulConfig(Operation operation, String msg, int rc, ConfigSerializable object) {
        super(operation, msg, rc, (String)null);
        MConfig cfg = new MConfig();
        if (object != null)
            try {   
                object.readSerializableConfig(cfg);
            } catch (Exception e) {
                throw new MRuntimeException(getOperationPath(), msg,e);
            }
        setResult(cfg);
    }

    @SuppressWarnings("deprecation")
    public SuccessfulConfig(String path, String msg, ConfigSerializable object) {
        super(path, msg, 0, (String)null);
        MConfig cfg = new MConfig();
        if (object != null)
            try {   
                object.readSerializableConfig(cfg);
            } catch (Exception e) {
                throw new MRuntimeException(getOperationPath(), msg,e);
            }
        setResult(cfg);
    }

    @SuppressWarnings("deprecation")
    public SuccessfulConfig(String path, String msg, int rc, ConfigSerializable object) {
        super(path, msg, rc, (String)null);
        MConfig cfg = new MConfig();
        if (object != null)
            try {   
                object.readSerializableConfig(cfg);
            } catch (Exception e) {
                throw new MRuntimeException(getOperationPath(), msg,e);
            }
        setResult(cfg);
    }
    
    public SuccessfulConfig(Operation operation, String msg, String... keyValues) {
        this(operation.getDescription().getPath(), msg, 0, keyValues);
        setCaption(operation.getDescription().getCaption());
    }

    @SuppressWarnings("deprecation")
    public SuccessfulConfig(String path, String msg, int rc, String... keyValues) {
        super(path, msg, rc, (String)null);
        setOperationPath(path);
        setCaption("");
        setMsg(msg);
        setReturnCode(rc);
        setSuccessful(true);
        MConfig r = new MConfig();
        if (keyValues != null) {
            for (int i = 0; i < keyValues.length - 1; i += 2)
                if (keyValues.length > i + 1) r.put(keyValues[i], keyValues[i + 1]);
            setResult(r);
        }
    }

    public SuccessfulConfig(Operation operation, String msg, int rc, String... keyValues) {
        this(operation.getDescription().getPath(), msg, rc, keyValues);
        setCaption(operation.getDescription().getCaption());
    }

    @SuppressWarnings("deprecation")
    public IConfig getConfig() {
        return (IConfig) getResult();
    }

    public void put(String key, Object value) {
        getConfig().put(key, value);
    }

    public Object get(String key) {
        return getConfig().get(key);
    }

    public void remove(String key) {
        getConfig().remove(key);
    }

    public Set<String> keySet() {
        return getConfig().keySet();
    }

    public int size() {
        return getConfig().size();
    }
    
    public boolean isObject(String key) {
        return getConfig().isObject(key);
    }

    public IConfig getObjectOrNull(String key) {
        return getConfig().getObjectOrNull(key);
    }

    public IConfig getObject(String key) throws NotFoundException {
        return getConfig().getObject(key);
    }

    public boolean isArray(String key) {
        return getConfig().isArray(key);
    }

    public ConfigList getArray(String key) throws NotFoundException {
        return getConfig().getArray(key);
    }

    public IConfig getObjectByPath(String path) {
        return getConfig().getObjectByPath(path);
    }

    public String getExtracted(String key, String def) {
        return getConfig().getExtracted(key, def);
    }

    public String getExtracted(String key) {
        return getConfig().getExtracted(key);
    }

    public List<IConfig> getObjects() {
        return getConfig().getObjects();
    }

    public void setObject(String key, IConfig object) {
        getConfig().setObject(key, object);
    }

    public void setObject(String key, ConfigSerializable object) {
        getConfig().setObject(key, object);
    }
    
    public IConfig createObject(String key) {
        return getConfig().createObject(key);
    }

    public List<String> getPropertyKeys() {
        return getConfig().getPropertyKeys();
    }

    public List<String> getObjectKeys() {
        return getConfig().getObjectKeys();
    }

    /**
     * Return in every case a list. An Array List or list with a single Object or a object with
     * nameless value or an empty list.
     *
     * @param key
     * @return A list
     */
    public ConfigList getList(String key) {
        return getConfig().getList(key);
    }

    /**
     * Return a iterator over a array or a single object. Return an empty iterator if not found. Use
     * this function to iterate over arrays or objects.
     *
     * @param key
     * @return Never null.
     */
    public List<IConfig> getObjectList(String key) {
        return getConfig().getObjectList(key);
    }

    public List<String> getObjectAndArrayKeys() {
        return getConfig().getObjectAndArrayKeys();
    }

    public List<String> getArrayKeys() {
        return getConfig().getArrayKeys();
    }

    public ConfigList getArrayOrNull(String key) {
        return getConfig().getArrayOrNull(key);
    }

    public ConfigList getArrayOrCreate(String key) {
        return getConfig().getArrayOrCreate(key);
    }

    public ConfigList createArray(String key) {
        return getConfig().createArray(key);
    }

}
