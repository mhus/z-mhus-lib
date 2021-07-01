/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.node;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.mhus.lib.basics.IsNull;
import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.M;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.parser.CompiledString;
import de.mhus.lib.core.util.SingleList;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.errors.MaxDepthReached;
import de.mhus.lib.errors.NotFoundException;

public class MNode extends MProperties implements INode {

    protected String name;
    protected INode parent;
    protected NodeStringCompiler compiler;
    protected HashMap<String, CompiledString> compiledCache;
    protected NodeList array;

    public MNode() {}

    public MNode(String name) {
        this.name = name;
    }
    
    public MNode(String name, NodeList array) {
        this.name = name;
        this.array = array;
        if (array != null)
        	this.parent = array.getParent();
    }
    
    public MNode(String name, INode parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public boolean isObject(String key) {
        Object val = get(key);
        if (val == null) return false;
        return val instanceof INode;
    }

    @Override
    public INode getObject(String key) throws NotFoundException {
        Object val = get(key);
        if (val == null) throw new NotFoundException("value not found", key);
        if (val instanceof INode) return (INode) val;
        throw new NotFoundException("value is not an INode", key);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public INode getAsObject(String key) {
        Object val = get(key);
        if (val == null) return null;
        if (val instanceof INode) return (INode) val;
        if (val instanceof IProperties) return new MNodeWrapper((IProperties) val);

        MNode ret = new MNode();
        if (val instanceof Map) {
            ret.putAll((Map)val);
        } else
            ret.put(NAMELESS_VALUE, val);
        return ret;
    }
    
    @Override
    public INode getObjectOrNull(String key) {
        Object val = get(key);
        if (val == null) return null;
        if (val instanceof INode) return (INode) val;
        return null;
    }

    @Override
    public boolean isArray(String key) {
        Object val = get(key);
        if (val == null) return false;
        return val instanceof NodeList;
    }

    @Override
    public NodeList getArray(String key) throws NotFoundException {
        Object val = get(key);
        if (val == null) throw new NotFoundException("value not found", key);
        if (val instanceof List) return (NodeList) val;
        throw new NotFoundException("value is not a NodeList", key);
    }

    @Override
    public NodeList getArrayOrNull(String key) {
        Object val = get(key);
        if (val == null) return null;
        if (val instanceof NodeList) return (NodeList) val;
        return null;
    }

    @Override
    public NodeList getArrayOrCreate(String key) {
        Object val = get(key);
        if (val == null) return createArray(key);
        if (val instanceof NodeList) return (NodeList) val;
        return createArray(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<INode> getObjectList(String key) {
        Object val = get(key);
        if (val == null) return (List<INode>) M.EMPTY_LIST;
        // if (val == null) throw new NotFoundException("value not found",key);
        if (val instanceof INode) return new SingleList<INode>((INode) val);
        if (val instanceof NodeList) return Collections.unmodifiableList((NodeList) val);
        return (List<INode>) M.EMPTY_LIST;
        // throw new NotFoundException("value is not a NodeList or INode",key);
    }

    @Override
    public INode getObjectByPath(String path) {
        if (path == null) return null;
        if (path.equals("") || path.equals(".")) return this;
        while (path.startsWith("/")) path = path.substring(1);
        if (path.length() == 0) return this;
        int p = path.indexOf('/');
        if (p < 0) return getObjectOrNull(path);
        INode next = getObjectOrNull(path.substring(0, p));
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
    public NodeList getList(String key) {
        if (isArray(key)) return getArrayOrNull(key);
        if (isObject(key)) {
            NodeList ret = new NodeList(key, this);
            ret.add(getObjectOrNull(key));
            return ret;
        }
        if (containsKey(key)) {
            NodeList ret = new NodeList(key, this);
            MNode obj = new MNode(key, this);
            obj.put(NAMELESS_VALUE, get(key));
            ret.add(obj);
            return ret;
        }
        return new NodeList(key, this);
    }

    protected String getExtracted(String key, String def, int level) {

        if (level > 10) return def;

        String value = getString(key, null);

        if (value == null) return def;
        if (value.indexOf('$') < 0) return value;

        synchronized (this) {
            if (compiler == null) {
                compiler = new NodeStringCompiler(this);
                compiledCache = new HashMap<String, CompiledString>();
            }
            CompiledString cached = compiledCache.get(key);
            if (cached == null) {
                cached = compiler.compileString(value);
                compiledCache.put(key, cached);
            }
            try {
                return cached.execute(new NodeStringCompiler.NodeMap(level, this));
            } catch (MException e) {
                throw new MRuntimeException(key, e);
            }
        }
    }

    @Override
    public List<INode> getObjects() {
        ArrayList<INode> out = new ArrayList<>();
        for (Object val : values()) {
            if (val instanceof INode) out.add((INode) val);
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public void setObject(String key, INode object) {
        if (object == null) {
            remove(key);
            return;
        }
        ((MNode) object).parent = this;
        ((MNode) object).name = key;
        put(key, object);
    }

    @Override
    public void addObject(String key, INode object) {
        Object obj = get(key);
        if (obj != null) {
            if (obj instanceof NodeList) {
                ((NodeList) obj).add(object);
            } else if (obj instanceof INode) {
                LinkedList<Object> list = new LinkedList<>();
                list.add(obj);
                put(key, obj);
                list.add(object);
            } else {
                // overwrite non object and arrays
                NodeList list = new NodeList(key, this);
                put(key, list);
                list.add(object);
            }
        } else {
            setObject(key, object);
            return;
        }
    }

    @Override
    public INode setObject(String key, NodeSerializable object) {
        INode cfg = createObject(key);
        try {
            object.writeSerializabledNode(cfg);
        } catch (Exception e) {
            throw new MRuntimeException(e);
        }
        return cfg;
    }

    @Override
    public INode createObject(String key) {
        INode obj = new MNode();
        addObject(key, obj);
        return obj;
    }

    @Override
    public NodeList createArray(String key) {
        NodeList list = new NodeList(key, this);
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
            if (!(entry.getValue() instanceof INode) && !(entry.getValue() instanceof NodeList))
                out.add(entry.getKey());
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public INode getParent() {
        return parent;
    }

    @Override
    public NodeList getParentArray() {
        return array;
    }
    
    @Override
    public List<String> getObjectKeys() {
        ArrayList<String> out = new ArrayList<>();
        for (Entry<String, Object> entry : entrySet()) {
            if (entry.getValue() instanceof INode) out.add(entry.getKey());
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public List<String> getArrayKeys() {
        ArrayList<String> out = new ArrayList<>();
        for (Entry<String, Object> entry : entrySet()) {
            if (entry.getValue() instanceof NodeList) out.add(entry.getKey());
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public List<String> getObjectAndArrayKeys() {
        ArrayList<String> out = new ArrayList<>();
        for (Entry<String, Object> entry : entrySet()) {
            if (entry.getValue() instanceof INode || entry.getValue() instanceof NodeList)
                out.add(entry.getKey());
        }
        return Collections.unmodifiableList(out);
    }

    @Override
    public synchronized String toString() {
        return (name == null || array != null ? "" : name + ":") + super.toString();
    }

    @Override
    public boolean isProperties() {
        for (Object val : values())
            if ((val instanceof NodeList) || (val instanceof INode))
                return false;
        return true;
    }
    

    public void putMapToNode(Map<?, ?> m) {
        putMapToNode(m, 0);
    }

    protected void putMapToNode(Map<?, ?> m, int level) {
        if (level > M.MAX_DEPTH_LEVEL) throw new MaxDepthReached();
        for (Map.Entry<?, ?> e : m.entrySet())
            if (e.getValue() instanceof IsNull) remove(e.getKey());
            else {
                if (e.getValue() instanceof Map) {
                    MNode cfg = new MNode();
                    cfg.putMapToNode((Map<?,?>)e.getValue(), level+1);
                    put(String.valueOf(e.getKey()), cfg);
                } else
                if (e.getValue() instanceof List) {
                    NodeList list = new NodeList(String.valueOf(e.getKey()), null);
                    put(String.valueOf(e.getKey()), list);
                    for (Object obj : ((List<?>)e.getValue())) {
                        if (obj instanceof INode) {
                            list.add((INode)obj);
                        } else {
                            MNode cfg = (MNode) list.createObject();
                            if (obj instanceof Map) {
                                cfg.putMapToNode((Map<?,?>)obj, level+1);
                            } else
                                cfg.put(NAMELESS_VALUE, obj);
                        }
                    }
                } else
                    put(String.valueOf(e.getKey()), e.getValue());
            }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(name);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        name = (String) in.readObject();
    }

}
