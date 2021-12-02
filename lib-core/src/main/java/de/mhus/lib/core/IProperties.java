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
package de.mhus.lib.core;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

import de.mhus.lib.core.node.INode;
import de.mhus.lib.core.util.MUri;

public interface IProperties
        extends IReadProperties,
                Map<String, Object>,
                Serializable,
                Iterable<Map.Entry<String, Object>> {

    void setProperty(String name, Object value);

    void setString(String name, String value);

    void setInt(String name, int value);

    void setLong(String name, long value);

    void setDouble(String name, double value);

    void setFloat(String name, float value);

    void setBoolean(String name, boolean value);

    void setCalendar(String name, Calendar value);

    void setDate(String name, Date value);

    void setNumber(String name, Number value);

    void removeProperty(String key);

    boolean isEditable();

    @Override
    void clear();

    String getFormatted(String name, String def, Object... values);

    /**
     * Creates an MProperties object and fills in the keys and values in alternating order.
     * @param keysAndValues
     * @return In every case a properties object
     */
    public static MProperties to(Object ... keysAndValues) {
        MProperties out = new MProperties();
        if (keysAndValues != null) {
            for (int i = 0; i < keysAndValues.length; i += 2) {
                if (i + 1 < keysAndValues.length)
                    out.put(String.valueOf(keysAndValues[i]), keysAndValues[i + 1]);
            }
        }
        return out;
    }

    /**
     * Creates an MProperties object and fills in the keys and values in alternating order.
     * @param keysAndValues
     * @return In every case a properties object
     */
    public static MProperties to(String ... keysAndValues) {
        MProperties out = new MProperties();
        if (keysAndValues != null) {
            for (int i = 0; i < keysAndValues.length; i += 2) {
                if (i + 1 < keysAndValues.length)
                    out.put(keysAndValues[i], keysAndValues[i + 1]);
            }
        }
        return out;
    }

    /**
     * This will handle the strings like options. Means a string without separator will handled as
     * key and set to true. e.g. val1&val2&a=b will be val1=true, val2=true, a=b
     *
     * @param properties Rfc1738 (Url Encode) encoded string
     * @return The MProperties
     */
    public static MProperties explodeToOptions(String properties) {
        return explodeToOptions(MUri.explodeArray(properties), '=');
    }

    /**
     * This will handle the strings like options. Means a string without separator will handled as
     * key and set to true. e.g. [val1, val2, a=b] will be val1=true, val2=true, a=b
     *
     * @param properties
     * @return The MProperties
     */
    public static MProperties explodeToOptions(String[] properties) {
        return explodeToOptions(properties, '=');
    }

    /**
     * This will handle the strings like properties. Means a string without separator will be stored
     * as value with an increasing key as integer, e.g. val1&val2&a=b will be 0=val1, 1=val2, a=b
     *
     * @param properties Rfc1738 (Url Encoded) encoded string
     * @return The MProperties
     */
    public static MProperties explodeToMProperties(String properties) {
        return explodeToMProperties(MUri.explodeArray(properties), '=', ':', 0, Integer.MAX_VALUE);
    }

    /**
     * This will transfer the property to a string using Rfc1738 url encoding
     *
     * @param properties
     * @return The String
     */
    public static String implode(IProperties properties) {
        return MUri.implode(properties);
    }

    /**
     * This will handle the strings like properties. Means a string without separator will be stored
     * as value with an increasing key as integer, e.g. [val1, val2, a=b] will be 0=val1, 1=val2,
     * a=b
     *
     * @param properties
     * @return The MProperties
     */
    public static MProperties explodeToMProperties(String[] properties) {
        if (properties == null) return new MProperties();
        return explodeToMProperties(properties, '=', ':', 0, properties.length);
    }

    public static MProperties explodeToMProperties(String[] properties, int offset, int length) {
        if (properties == null) return new MProperties();
        return explodeToMProperties(properties, '=', ':', offset, length);
    }

    /**
     * This will handle the strings like options. Means a string without separator will handled as
     * key and set to true. e.g. [val1, val2, a=b] will be val1=true, val2=true, a=b
     *
     * @param properties
     * @param separator
     * @return The MProperties
     */
    public static MProperties explodeToOptions(String[] properties, char separator) {
        MProperties p = new MProperties();
        if (properties != null) {
            for (String i : properties) {
                if (i != null) {
                    int idx = i.indexOf(separator);
                    if (idx >= 0) {
                        p.setProperty(i.substring(0, idx).trim(), i.substring(idx + 1));
                    } else {
                        p.setProperty(i, true);
                    }
                }
            }
        }
        return p;
    }

    /**
     * This will handle the strings like properties. Means a string without separator will be stored
     * as value with an increasing key as integer, e.g. [val1, val2, a=b] will be 0=val1, 1=val2,
     * a=b
     *
     * @param properties
     * @param keySeparator
     * @param typeSeparator
     * @return The MProperties
     */
    public static MProperties explodeToMProperties(
            String[] properties, char keySeparator, char typeSeparator) {
        return explodeToMProperties(properties, keySeparator, typeSeparator, 0, Integer.MAX_VALUE);
    }

    public static MProperties explodeToMProperties(
            String[] properties, char keySeparator, char typeSeparator, int offset, int length) {
        MProperties p = new MProperties();
        if (properties != null) {
            for (int i = 0; i < length; i++) {
                int pos = i + offset;
                if (pos >= properties.length) break;
                String item = properties[pos];
                if (item != null) appendToMap(p, item, keySeparator, typeSeparator);
            }
        }
        return p;
    }

    /**
     * This will handle the strings like properties. Means a string without separator will be stored
     * as value with an increasing key as integer, e.g. [val1, val2, a=b] will be 0=val1, 1=val2,
     * a=b
     *
     * @param properties
     * @return The Properties
     */
    public static Properties explodeToProperties(String[] properties) {
        Properties p = new Properties();
        if (properties != null) {
            for (String i : properties) {
                if (i != null) {
                    appendToMap(p, i, '=', ':');
                }
            }
        }
        return p;
    }

    /**
     * In this scenario we separate between functional parameters (starting with one underscore) and
     * data. Using this method functional parameters can be cascaded over multiple levels.
     *
     * <p>Will remove all parameters starting with underscore and not two underscore and remove one
     * underscore from thoos with more underscores.
     *
     * <p>_test will be removed __test will be _test
     *
     * <p>after update
     *
     * @param in
     */
    public static <V> void updateFunctional(Map<String, V> in) {
        in.keySet().removeIf(k -> isFunctional(k));
        for (String key : new LinkedList<>(in.keySet()))
            if (key.startsWith("_")) in.put(key.substring(1), in.remove(key));
    }

    /**
     * Return true if key starts with underscore but not with two underscores.
     *
     * @param key
     * @return true if actual internal
     */
    public static boolean isFunctional(String key) {
        return key.startsWith("_") && !key.startsWith("__");
    }

    public static IProperties toIProperties(IReadProperties properties) {
        if (properties == null) return null;
        if (properties instanceof IProperties) return (IProperties) properties;
        return new MProperties(properties);
    }

    public static MProperties toMProperties(IReadProperties properties) {
        if (properties == null) return null;
        if (properties instanceof MProperties) return (MProperties) properties;
        return new MProperties(properties);
    }

    public static void appendToMap(Map<?, ?> p, String para) {
        appendToMap(p, para, '=', ':');
    }

    @SuppressWarnings("unchecked")
    public static void appendToMap(
            Map<?, ?> p, String para, char keySeparator, char typeSeparator) {
        if (para == null) return;
        int pos = para.indexOf(keySeparator);
        if (pos < 0) {
            String t = "text";
            if (typeSeparator != 0) {
                pos = para.indexOf(typeSeparator);
                if (pos > 0) {
                    t = para.substring(pos + 1);
                    para = para.substring(0, pos);
                }
            }
            Object obj = MCast.toType(para, t, null);
            if (obj != null) ((Map<Object, Object>) p).put(INode.NAMELESS_VALUE, para);
            return;
        }
        String k = para.substring(0, pos).trim();
        String v = para.substring(pos + 1);
        String t = "text";
        if (typeSeparator != 0) {
            pos = k.indexOf(typeSeparator);
            if (pos > 0) {
                t = k.substring(pos + 1);
                k = k.substring(0, pos);
            }
        }
        Object obj = MCast.toType(v, t, null);
        if (obj != null) ((Map<Object, Object>) p).put(k, obj);
    }

    /**
     * Extract the keys starting with prefix in a new HashMap. Will return an empty map if prefix or
     * map is null.
     *
     * @param prefix Prefix of the key to extract
     * @param map Map of all entries
     * @return Extracted subset
     */
    static IProperties subset(String prefix, Map<String, ?> map) {
        MProperties out = new MProperties();
        if (prefix == null || map == null) return out;
        map.forEach(
                (k, v) -> {
                    if (k.startsWith(prefix)) out.put(k, v);
                });
        return out;
    }

    /**
     * Extract the keys starting with prefix in a new HashMap. It removes the prefix from the keys.
     * Will return an empty map if prefix or map is null.
     *
     * @param prefix Prefix of the key to extract
     * @param map Map of all entries
     * @return Extracted subset
     */
    static IProperties subsetCrop(String prefix, Map<String, ?> map) {
        MProperties out = new MProperties();
        if (prefix == null || map == null) return out;
        int l = prefix.length();
        map.forEach(
                (k, v) -> {
                    if (k.startsWith(prefix)) out.put(k.substring(l), v);
                });
        return out;
    }
}
