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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.mhus.lib.basics.RC;
import de.mhus.lib.core.MCollection;
import de.mhus.lib.core.MDate;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.cfg.CfgInt;
import de.mhus.lib.core.pojo.MPojo;
import de.mhus.lib.core.util.NullValue;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.errors.TooDeepStructuresException;

public class PropertiesNodeBuilder extends INodeBuilder {

    protected static final CfgInt CFG_MAX_LEVEL =
            new CfgInt(PropertiesNodeBuilder.class, "maxLevel", 100);

    @Override
    public INode read(InputStream is) throws MException {
        try {
            MProperties p = MProperties.load(is);
            return readFromMap(p);
        } catch (IOException e) {
            throw new MException(RC.STATUS.ERROR, e);
        }
    }

    @Override
    public void write(INode node, OutputStream os) throws MException {
        MProperties p = new MProperties(node);
        try {
            p.save(os);
        } catch (IOException e) {
            throw new MException(RC.STATUS.ERROR, e);
        }
    }

    public INode readFromMap(Map<?, ?> map) {
        return readFromMap(map, 0);
    }

    public INode readFromCollection(Collection<?> col) {
        INode node = new MNode();
        readFromCollection(node, INode.NAMELESS_VALUE, col, 0);
        return node;
    }

    protected void readFromCollection(INode node, String key, Collection<?> col, int level) {
        level++;
        if (level > CFG_MAX_LEVEL.value()) throw new TooDeepStructuresException();

        NodeList arr = node.createArray(key);
        for (Object item : col) {
            INode obj = readObject(item, level);
            arr.add(obj);
        }
    }

    protected INode readFromMap(Map<?, ?> map, int level) {
        level++;
        if (level > CFG_MAX_LEVEL.value()) throw new TooDeepStructuresException();

        INode node = new MNode();
        for (Entry<?, ?> entry : map.entrySet()) {
            String key = MString.valueOf(entry.getKey());
            Object val = entry.getValue();
            if (val == null || val instanceof NullValue) {
                // null object or ignore ?
            } else if (val instanceof String
                    || val.getClass().isPrimitive()
                    || val instanceof Number
                    || val instanceof Date
                    || val instanceof Boolean) {
                node.put(key, val);
            } else {
                INode obj = readObject(val, level);
                if (obj.isProperty(INode.NAMELESS_VALUE)) {
                    node.put(key, obj.get(INode.NAMELESS_VALUE));
                    if (node.isProperty(INode.HELPER_VALUE))
                        node.put(INode.HELPER_VALUE + key, node.get(INode.HELPER_VALUE));
                } else if (obj.isObject(INode.NAMELESS_VALUE)) {
                    node.addObject(key, obj.getObjectOrNull(INode.NAMELESS_VALUE));
                } else node.addObject(key, obj);
            }
        }
        return node;
    }

    public INode readObject(Object item) {
        return readObject(item, 0);
    }

    protected INode readObject(Object item, int level) {
        level++;
        if (level > CFG_MAX_LEVEL.value()) throw new TooDeepStructuresException();

        if (item == null) {
            MNode obj = new MNode();
            obj.setBoolean(INode.NULL, true);
            return obj;
        } else if (item instanceof NodeSerializable) {
            MNode obj = new MNode();
            try {
                ((NodeSerializable) item).writeSerializabledNode(obj);
            } catch (Exception e) {
                throw new MRuntimeException(RC.STATUS.ERROR, item, e);
            }
            return obj;
        } else if (item instanceof INode) {
            return (INode) item;
        } else if (item instanceof Map) {
            INode obj = readFromMap((Map<?, ?>) item, level);
            return obj;
        } else if (item instanceof String
                || item.getClass().isPrimitive()
                || item instanceof Number
                || item instanceof Date
                || item instanceof Boolean) {
            MNode obj = new MNode();
            obj.put(INode.NAMELESS_VALUE, item);
            return obj;
        } else if (item instanceof Date) {
            MNode obj = new MNode();
            obj.put(INode.NAMELESS_VALUE, ((Date) item).getTime());
            obj.put(INode.HELPER_VALUE, MDate.toIso8601((Date) item));
            return obj;
        } else if (item.getClass().isArray()) {
            MNode obj = new MNode();
            obj.setString(INode.CLASS, item.getClass().getCanonicalName());
            readFromCollection(
                    obj, INode.NAMELESS_VALUE, MCollection.toList(((Object[]) item)), level);
            return obj;
        } else if (item instanceof Collection) {
            MNode obj = new MNode();
            obj.setString(INode.CLASS, item.getClass().getCanonicalName());
            readFromCollection(obj, INode.NAMELESS_VALUE, (Collection<?>) item, level);
            return obj;
        } else {
            MNode obj = new MNode();
            try {
                MPojo.pojoToNode(item, obj);
            } catch (IOException e) {
                throw new MRuntimeException(RC.STATUS.ERROR, item, e);
            }
            return obj;
        }
    }

    public <T extends NodeSerializable> List<T> loadToCollection(INode source, Class<T> target)
            throws Exception {
        ArrayList<T> out = new ArrayList<>();
        for (INode entry : source.getArray(INode.NAMELESS_VALUE)) {
            T inst = target.getConstructor().newInstance();
            inst.readSerializabledNode(entry);
            out.add(inst);
        }
        return out;
    }

    public <V extends NodeSerializable> Map<String, V> loadToMap(INode source, Class<V> target)
            throws Exception {
        HashMap<String, V> out = new HashMap<>();
        for (INode entry : source.getObjects()) {
            V inst = target.getConstructor().newInstance();
            inst.readSerializabledNode(entry);
            out.put(entry.getName(), inst);
        }
        return out;
    }
}
