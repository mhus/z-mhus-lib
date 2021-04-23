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
package de.mhus.lib.core.config;

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
 * A IConfig extends the concept of properties to a object oriented structure. A property can also
 * be an object or array of objects. The IConfig will not really separate objects and arrays. If you
 * require an array and it's only a single objects you will get a list with a single object and vies
 * versa.
 *
 * @author mikehummel
 */
public interface IConfig extends IProperties {

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

    IConfig getObjectOrNull(String key);

    IConfig getObject(String key) throws NotFoundException;

    boolean isArray(String key);

    ConfigList getArray(String key) throws NotFoundException;

    IConfig getObjectByPath(String path);

    String getExtracted(String key, String def);

    String getExtracted(String key);

    List<IConfig> getObjects();

    void setObject(String key, IConfig object);

    /**
     * Add the Object to a list of objects named with key.
     * @param key
     * @param object
     */
    void addObject(String key, IConfig object);

    IConfig setObject(String key, ConfigSerializable object);

    IConfig createObject(String key);

    List<String> getPropertyKeys();

    String getName();

    IConfig getParent();

    List<String> getObjectKeys();

    /**
     * Return in every case a list. An Array List or list with a single Object or a object with
     * nameless value or an empty list.
     *
     * @param key
     * @return A list
     */
    ConfigList getList(String key);

    /**
     * Return a iterator over a array or a single object. Return an empty iterator if not found. Use
     * this function to iterate over arrays or objects.
     *
     * @param key
     * @return Never null.
     */
    List<IConfig> getObjectList(String key);

    List<String> getObjectAndArrayKeys();

    List<String> getArrayKeys();

    ConfigList getArrayOrNull(String key);

    ConfigList getArrayOrCreate(String key);

    ConfigList createArray(String key);

    //    IConfig cloneObject(IConfig node);

    default <T extends ConfigSerializable> T load(T fillIn) {
        if (fillIn == null) return null;
        if (getBoolean(NULL, false))
            return null;
        try {
            fillIn.readSerializableConfig(this);
        } catch (Exception e) {
            throw new MRuntimeException(fillIn,this,e);
        }
        return fillIn;
    }

    /**
     * Transform a object into IConfig.
     * 
     * @param object
     * @return IConfig 
     * @throws Exception
     */
    static IConfig read(ConfigSerializable object) throws Exception {
        IConfig cfg = new MConfig();
        if (object == null)
            cfg.setBoolean(IConfig.NULL, true);
        else
            object.writeSerializableConfig(cfg);
        return cfg;
    }

    /**
     * Return a config or null if the string is not understand.
     *
     * @param configString
     * @return A config object if the config is found or null. If no config is recognized it returns
     *     null
     * @throws MException
     */
    static IConfig readConfigFromString(String configString) throws MException {
        if (MString.isEmptyTrim(configString)) return new MConfig();
        if (configString.startsWith("[") || configString.startsWith("{")) {
            try {
                return readFromJsonString(configString);
            } catch (Exception e) {
                throw new MException(configString, e);
            }
        }
        if (configString.startsWith("<?")) {
            try {
                return readFromXmlString(MXml.loadXml(configString).getDocumentElement());
            } catch (Exception e) {
                throw new MException(configString, e);
            }
        }

        if (configString.contains("=")) {
            if (configString.contains("&"))
                return readFromProperties(new HashMap<>(MUri.explode(configString)));
            else return readFromProperties(IProperties.explodeToMProperties(configString));
        }

        return null;
    }

    /**
     * Return a config or null if the string is not understand.
     *
     * @param configStrings
     * @return IConfig, never null
     * @throws MException
     */
    static IConfig readConfigFromString(String[] configStrings) throws MException {
        if (configStrings == null || configStrings.length == 0) return new MConfig();
        if (configStrings.length == 1) return readConfigFromString(configStrings[0]);
        return readFromProperties(IProperties.explodeToMProperties(configStrings));
    }

    static IConfig readFromProperties(Map<String, Object> lines) {
        return new PropertiesConfigBuilder().readFromMap(lines);
    }

    static IConfig readFromMap(Map<?, ?> lines) {
        return new PropertiesConfigBuilder().readFromMap(lines);
    }

    static <V extends ConfigSerializable> Map<String,V> loadToMap(IConfig source, Class<V> target) throws Exception {
        return new PropertiesConfigBuilder().loadToMap(source, target);
    }
    
    static IConfig readFromCollection(Collection<?> lines) {
        return new PropertiesConfigBuilder().readFromCollection(lines);
    }

    static <T extends ConfigSerializable> List<T> loadToCollection(IConfig source, Class<T> target) throws Exception {
        return new PropertiesConfigBuilder().loadToCollection(source, target);
    }

    static IConfig readFromJsonString(String json) throws MException {
        return new JsonConfigBuilder().readFromString(json);
    }

    static IConfig readFromXmlString(Element documentElement) throws MException {
        return new XmlConfigBuilder().readFromElement(documentElement);
    }

    static IConfig readFromYamlString(String yaml) throws MException {
        return new YamlConfigBuilder().readFromString(yaml);
    }

    static String toCompactJsonString(IConfig config) throws MException {
        try {
            return MJson.toString(new JsonConfigBuilder().writeToJsonNode(config));
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    static String toPrettyJsonString(IConfig config) throws MException {
        try {
            return MJson.toPrettyString(new JsonConfigBuilder().writeToJsonNode(config));
        } catch (IOException e) {
            throw new MException(e);
        }
    }

    public static void merge(IConfig from, IConfig to) throws MException {
        merge(from, to, 0);
    }

    private static void merge(IConfig from, IConfig to, int level) throws MException {
        if (level > 100) throw new TooDeepStructuresException();
        
        for (IConfig node : from.getObjects()) {
            IConfig n = to.createObject(node.getName());
            for (String name : node.getPropertyKeys()) {
                n.put(name, node.get(name));
            }
            merge(node, (IConfig) n, level + 1);
        }
        for (String key : from.getArrayKeys()) {
            ConfigList toArray = to.createArray(key);
            for (IConfig node : from.getArrayOrNull(key)) {
                IConfig n = toArray.createObject();
                for (String name : ((IConfig)node).getPropertyKeys()) {
                    n.put(name, node.get(name));
                }
                merge(node, (IConfig) n, level + 1);
            }
        }
        for (String name : from.getPropertyKeys()) {
            to.put(name, from.get(name));
        }

    }

    public static String[] toStringArray(Collection<IConfig> nodes, String key) {
        LinkedList<String> out = new LinkedList<>();
        for (IConfig item : nodes) {
            String value = item.getString(key, null);
            if (value != null) out.add(value);
        }
        return out.toArray(new String[out.size()]);
    }

    /**
     * Try to un serialize the object with the config. If it fails null will be returned.
     * @param <T> Type
     * @param cfg Config with serialized data
     * @param fillIn The object to fill
     * @return The fillIn object or null
     */
    public static <T extends ConfigSerializable> T loadOrNull(IConfig cfg, T fillIn) {
        if (fillIn == null || cfg == null) return null;
        try {
            fillIn.readSerializableConfig(cfg);
        } catch (Exception e) {
            MLogUtil.log().d(cfg,e);
            return null;
        }
        return fillIn;
    }

    /**
     * Un serialize the object with the config.
     * @param <T> Type
     * @param cfg Config with serialized data
     * @param fillIn The object to fill
     * @return The fillIn object
     */
    public static <T extends ConfigSerializable> T load(IConfig cfg, T fillIn) {
        if (fillIn == null || cfg == null) return null;
        try {
            fillIn.readSerializableConfig(cfg);
        } catch (Exception e) {
            MLogUtil.log().d(cfg,e);
            return null;
        }
        return fillIn;
    }

    /**
     * Return a wrapped parameter to config object. If the wrapped
     * object is changes also values in the original object will be changed.
     * 
     * @param parameters
     * @return A wrapping IConfig object
     */
	static IConfig wrap(IProperties parameters) {
		return new MConfigWrapper(parameters);
	}

	/**
	 * Return true if no value is in list from type IConfig or ConfigList.
	 * Other Objects will be seen as flat.
	 * 
	 * @return true if compatible with IProperties
	 */
    boolean isProperties();

    /**
     * Return the value in every case as IConfig object. Even if it's not found it will return null.
     * The result could be a new object not attached to the underlying map. Changes may have no affect to the
     * parent config.
     * 
     * @param key
     * @return The IConfig
     */
    IConfig getAsObject(String key);

}
