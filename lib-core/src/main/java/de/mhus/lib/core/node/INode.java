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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.logging.MLogUtil;
import de.mhus.lib.core.util.MUri;
import de.mhus.lib.errors.MException;
import de.mhus.lib.errors.MRuntimeException;
import de.mhus.lib.errors.NotFoundException;
import de.mhus.lib.errors.TooDeepStructuresException;

/**
 * A INode extends the concept of properties to a object oriented structure. A property can also
 * be an object or array of objects. The INode will not really separate objects and arrays. If you
 * require an array and it's only a single objects you will get a list with a single object and vies
 * versa.
 *
 * @author mikehummel
 */
public interface INode extends IProperties {

    public static final String NAMELESS_VALUE = "";
    public static final String VALUE = "value";
    public static final String VALUES = "values";
    public static final String ID = "_id";
    public static final String HELPER_VALUE = "_";
    public static final String CLASS = "_class";
    public static final String NULL = "_null";

    /**
     * Returns true if t@Override
    he key is an object.
     *
     * @param key
     * @return If the property is an object or array
     */
    boolean isObject(String key);

    INode getObjectOrNull(String key);

    INode getObject(String key) throws NotFoundException;

    boolean isArray(String key);

    NodeList getArray(String key) throws NotFoundException;

    INode getObjectByPath(String path);

    String getExtracted(String key, String def);

    String getExtracted(String key);

    List<INode> getObjects();

    void setObject(String key, INode object);

    /**
     * Add the Object to a list of objects named with key.
     * @param key
     * @param object
     */
    void addObject(String key, INode object);

    INode setObject(String key, NodeSerializable object);

    INode createObject(String key);

    List<String> getPropertyKeys();

    String getName();

    INode getParent();

    List<String> getObjectKeys();

    /**
     * Return in every case a list. An Array List or list with a single Object or a object with
     * nameless value or an empty list.
     *
     * @param key
     * @return A list
     */
    NodeList getList(String key);

    /**
     * Return a iterator over a array or a single object. Return an empty iterator if not found. Use
     * this function to iterate over arrays or objects.
     *
     * @param key
     * @return Never null.
     */
    List<INode> getObjectList(String key);

    List<String> getObjectAndArrayKeys();

    List<String> getArrayKeys();

    NodeList getArrayOrNull(String key);

    NodeList getArrayOrCreate(String key);

    NodeList createArray(String key);

    //    INode cloneObject(INode node);

    default <T extends NodeSerializable> T load(T fillIn) {
        if (fillIn == null) return null;
        if (getBoolean(NULL, false))
            return null;
        try {
            fillIn.readSerializabledNode(this);
        } catch (Exception e) {
            throw new MRuntimeException(fillIn,this,e);
        }
        return fillIn;
    }

    /**
     * Transform a object into INode.
     * 
     * @param object
     * @return INode 
     * @throws Exception
     */
    static INode read(NodeSerializable object) throws Exception {
        INode cfg = new MNode();
        if (object == null)
            cfg.setBoolean(INode.NULL, true);
        else
            object.writeSerializabledNode(cfg);
        return cfg;
    }

    /**
     * Return a node or null if the string is not understand.
     *
     * @param nodeString
     * @return A node object if the node is found or null. If no node is recognized it returns
     *     null
     * @throws MException
     */
    static INode readNodeFromString(String nodeString) throws MException {
        if (MString.isEmptyTrim(nodeString)) return new MNode();
        if (nodeString.startsWith("[") || nodeString.startsWith("{")) {
            try {
                return readFromJsonString(nodeString);
            } catch (Exception e) {
                throw new MException(nodeString, e);
            }
        }
        if (nodeString.startsWith("<?")) {
            try {
                return readFromXmlString(MXml.loadXml(nodeString).getDocumentElement());
            } catch (Exception e) {
                throw new MException(nodeString, e);
            }
        }

        if (nodeString.contains("=")) {
            if (nodeString.contains("&"))
                return readFromProperties(new HashMap<>(MUri.explode(nodeString)));
            else return readFromProperties(IProperties.explodeToMProperties(nodeString));
        }

        return null;
    }

    /**
     * Return a node or null if the string is not understand.
     *
     * @param nodeStrings
     * @return INode, never null
     * @throws MException
     */
    static INode readNodeFromString(String[] nodeStrings) throws MException {
        if (nodeStrings == null || nodeStrings.length == 0) return new MNode();
        if (nodeStrings.length == 1) return readNodeFromString(nodeStrings[0]);
        return readFromProperties(IProperties.explodeToMProperties(nodeStrings));
    }

    static INode readFromProperties(Map<String, Object> lines) {
        return new PropertiesNodeBuilder().readFromMap(lines);
    }

    static INode readFromMap(Map<?, ?> lines) {
        return new PropertiesNodeBuilder().readFromMap(lines);
    }

    static <V extends NodeSerializable> Map<String,V> loadToMap(INode source, Class<V> target) throws Exception {
        return new PropertiesNodeBuilder().loadToMap(source, target);
    }
    
    static INode readFromCollection(Collection<?> lines) {
        return new PropertiesNodeBuilder().readFromCollection(lines);
    }

    static <T extends NodeSerializable> List<T> loadToCollection(INode source, Class<T> target) throws Exception {
        return new PropertiesNodeBuilder().loadToCollection(source, target);
    }

    static INode readFromJsonString(String json) throws MException {
        return new JsonNodeBuilder().readFromString(json);
    }

    static INode readFromXmlString(Element documentElement) throws MException {
        return new XmlNodeBuilder().readFromElement(documentElement);
    }

    static INode readFromYamlString(String yaml) throws MException {
        return new YamlNodeBuilder().readFromString(yaml);
    }

    static String toCompactJsonString(INode node) throws MException {
        try {
            return MJson.toString(new JsonNodeBuilder().writeToJsonNode(node));
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    static String toPrettyJsonString(INode node) throws MException {
        try {
            return MJson.toPrettyString(new JsonNodeBuilder().writeToJsonNode(node));
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    public static void merge(INode from, INode to) throws MException {
        merge(from, to, 0);
    }

    private static void merge(INode from, INode to, int level) throws MException {
        if (level > 100) throw new TooDeepStructuresException();
        
        for (INode node : from.getObjects()) {
            INode n = to.createObject(node.getName());
            for (String name : node.getPropertyKeys()) {
                n.put(name, node.get(name));
            }
            merge(node, (INode) n, level + 1);
        }
        for (String key : from.getArrayKeys()) {
            NodeList toArray = to.createArray(key);
            for (INode node : from.getArrayOrNull(key)) {
                INode n = toArray.createObject();
                for (String name : ((INode)node).getPropertyKeys()) {
                    n.put(name, node.get(name));
                }
                merge(node, (INode) n, level + 1);
            }
        }
        for (String name : from.getPropertyKeys()) {
            to.put(name, from.get(name));
        }

    }

    public static String[] toStringArray(Collection<INode> nodes, String key) {
        LinkedList<String> out = new LinkedList<>();
        for (INode item : nodes) {
            String value = item.getString(key, null);
            if (value != null) out.add(value);
        }
        return out.toArray(new String[out.size()]);
    }

    /**
     * Try to un serialize the object with the node. If it fails null will be returned.
     * @param <T> Type
     * @param node Node with serialized data
     * @param fillIn The object to fill
     * @return The fillIn object or null
     */
    public static <T extends NodeSerializable> T loadOrNull(INode node, T fillIn) {
        if (fillIn == null || node == null) return null;
        try {
            fillIn.readSerializabledNode(node);
        } catch (Exception e) {
            MLogUtil.log().d(node,e);
            return null;
        }
        return fillIn;
    }

    /**
     * Un serialize the object with the node.
     * @param <T> Type
     * @param node Node with serialized data
     * @param fillIn The object to fill
     * @return The fillIn object
     */
    public static <T extends NodeSerializable> T load(INode node, T fillIn) {
        if (fillIn == null || node == null) return null;
        try {
            fillIn.readSerializabledNode(node);
        } catch (Exception e) {
            MLogUtil.log().d(node,e);
            return null;
        }
        return fillIn;
    }

    /**
     * Return a wrapped parameter to node object. If the wrapped
     * object is changes also values in the original object will be changed.
     * 
     * @param parameters
     * @return A wrapping INode object
     */
	static INode wrap(IProperties parameters) {
		return new MNodeWrapper(parameters);
	}

	/**
	 * Return true if no value is in list from type INode or NodeList.
	 * Other Objects will be seen as flat.
	 * 
	 * @return true if compatible with IProperties
	 */
    boolean isProperties();

    /**
     * Return the value in every case as INode object. Even if it's not found it will return null.
     * The result could be a new object not attached to the underlying map. Changes may have no affect to the
     * parent node.
     * 
     * @param key
     * @return The INode
     */
    INode getAsObject(String key);

}
