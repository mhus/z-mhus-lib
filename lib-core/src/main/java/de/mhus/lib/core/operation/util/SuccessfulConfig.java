package de.mhus.lib.core.operation.util;

import java.util.List;
import java.util.Set;

import de.mhus.lib.core.node.NodeList;
import de.mhus.lib.core.node.NodeSerializable;
import de.mhus.lib.core.node.INode;
import de.mhus.lib.core.node.MNode;
import de.mhus.lib.core.operation.Operation;
import de.mhus.lib.core.operation.Successful;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.errors.NotFoundException;

public class SuccessfulConfig extends Successful {
    
    public SuccessfulConfig(Operation operation, String msg) {
        super(operation, msg, 0, (String)null);
        setResultConfig(new MNode());
    }

    public SuccessfulConfig(Operation operation, String msg, INode config) {
        super(operation, msg, 0, (String)null);
        setResultConfig(config);
    }

    public SuccessfulConfig(Operation operation, String msg, int rc, INode config) {
        super(operation, msg, rc, (String)null);
        setResultConfig(config);
    }

    public SuccessfulConfig(String path, String msg, INode config) {
        super(path, msg, 0, (String)null);
        setResultConfig(config);
    }

    public SuccessfulConfig(String path, String msg, int rc, INode config) {
        super(path, msg, rc, (String)null);
        setResultConfig(config);
    }

    public SuccessfulConfig(Operation operation, String msg, NodeSerializable object) {
        super(operation, msg, 0, (String)null);
        MNode cfg = new MNode();
        if (object != null)
            try {   
                object.readSerializabledNode(cfg);
            } catch (Exception e) {
                throw new MRuntimeException(getOperationPath(), msg,e);
            }
        setResultConfig(cfg);
    }

    public SuccessfulConfig(Operation operation, String msg, int rc, NodeSerializable object) {
        super(operation, msg, rc, (String)null);
        MNode cfg = new MNode();
        if (object != null)
            try {   
                object.readSerializabledNode(cfg);
            } catch (Exception e) {
                throw new MRuntimeException(getOperationPath(), msg,e);
            }
        setResultConfig(cfg);
    }

    public SuccessfulConfig(String path, String msg, NodeSerializable object) {
        super(path, msg, 0, (String)null);
        MNode cfg = new MNode();
        if (object != null)
            try {   
                object.readSerializabledNode(cfg);
            } catch (Exception e) {
                throw new MRuntimeException(getOperationPath(), msg,e);
            }
        setResultConfig(cfg);
    }

    public SuccessfulConfig(String path, String msg, int rc, NodeSerializable object) {
        super(path, msg, rc, (String)null);
        MNode cfg = new MNode();
        if (object != null)
            try {   
                object.readSerializabledNode(cfg);
            } catch (Exception e) {
                throw new MRuntimeException(getOperationPath(), msg,e);
            }
        setResultConfig(cfg);
    }
    
    public SuccessfulConfig(Operation operation, String msg, String... keyValues) {
        this(operation.getDescription().getPath(), msg, 0, keyValues);
        setCaption(operation.getDescription().getCaption());
    }

    public SuccessfulConfig(String path, String msg, int rc, String... keyValues) {
        super(path, msg, rc, (String)null);
        setOperationPath(path);
        setCaption("");
        setMsg(msg);
        setReturnCode(rc);
        setSuccessful(true);
        MNode r = new MNode();
        if (keyValues != null) {
            for (int i = 0; i < keyValues.length - 1; i += 2)
                if (keyValues.length > i + 1) r.put(keyValues[i], keyValues[i + 1]);
            setResultConfig(r);
        }
    }

    public SuccessfulConfig(Operation operation, String msg, int rc, String... keyValues) {
        this(operation.getDescription().getPath(), msg, rc, keyValues);
        setCaption(operation.getDescription().getCaption());
    }

    public INode getConfig() {
        return getResultAsConfig();
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

    public INode getObjectOrNull(String key) {
        return getConfig().getObjectOrNull(key);
    }

    public INode getObject(String key) throws NotFoundException {
        return getConfig().getObject(key);
    }

    public boolean isArray(String key) {
        return getConfig().isArray(key);
    }

    public NodeList getArray(String key) throws NotFoundException {
        return getConfig().getArray(key);
    }

    public INode getObjectByPath(String path) {
        return getConfig().getObjectByPath(path);
    }

    public String getExtracted(String key, String def) {
        return getConfig().getExtracted(key, def);
    }

    public String getExtracted(String key) {
        return getConfig().getExtracted(key);
    }

    public List<INode> getObjects() {
        return getConfig().getObjects();
    }

    public void setObject(String key, INode object) {
        getConfig().setObject(key, object);
    }

    public void setObject(String key, NodeSerializable object) {
        getConfig().setObject(key, object);
    }
    
    public INode createObject(String key) {
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
    public NodeList getList(String key) {
        return getConfig().getList(key);
    }

    /**
     * Return a iterator over a array or a single object. Return an empty iterator if not found. Use
     * this function to iterate over arrays or objects.
     *
     * @param key
     * @return Never null.
     */
    public List<INode> getObjectList(String key) {
        return getConfig().getObjectList(key);
    }

    public List<String> getObjectAndArrayKeys() {
        return getConfig().getObjectAndArrayKeys();
    }

    public List<String> getArrayKeys() {
        return getConfig().getArrayKeys();
    }

    public NodeList getArrayOrNull(String key) {
        return getConfig().getArrayOrNull(key);
    }

    public NodeList getArrayOrCreate(String key) {
        return getConfig().getArrayOrCreate(key);
    }

    public NodeList createArray(String key) {
        return getConfig().createArray(key);
    }

}
