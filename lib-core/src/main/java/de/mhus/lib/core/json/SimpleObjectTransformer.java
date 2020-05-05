/**
 * Copyright 2018 Mike Hummel
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.core.json;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.errors.NotSupportedException;

public class SimpleObjectTransformer extends TransformStrategy {

    @SuppressWarnings("unchecked")
    @Override
    public Object jsonToPojo(JsonNode from, Class<?> type, TransformHelper helper)
            throws NotSupportedException {

        Object to;
        try {
            to = helper.createObject(type);
        } catch (Exception e1) {
            throw new NotSupportedException(type, e1);
        }

        if (from.size() == 1) {
            String singleName = from.fieldNames().next();
            if ("_collection".equals(singleName)) {
                LinkedList<Object> out = new LinkedList<>();
                for (JsonNode n : from.get(0)) {
                    out.add(jsonToPojo(n, null, helper.incLevel()));
                }
                helper.decLevel();
                return out;
            } else if ("_map".equals(singleName)) {
                HashMap<Object, Object> out = new HashMap<>();
                Iterator<String> nameIter = from.fieldNames();
                while (nameIter.hasNext()) {
                    String name = nameIter.next();
                    JsonNode on = from.get(name);
                    out.put(name, jsonToPojo(on, null, helper.incLevel()));
                }
                helper.decLevel();
                return out;
            }
        }

        PojoModel model = helper.createPojoModel(to);
        for (PojoAttribute<Object> attr : model) {
            String name = attr.getName();
            Class<?> aType = attr.getType();
            JsonNode json = from.get(helper.getPrefix() + name);

            try {
                if (json == null || !attr.canWrite()) {

                } else if (aType == Boolean.class || aType == boolean.class)
                    attr.set(to, json.asBoolean(false));
                else if (aType == Integer.class || aType == int.class) attr.set(to, json.asInt(0));
                else if (aType == String.class) attr.set(to, json.asText());
                else if (aType == UUID.class)
                    try {
                        attr.set(to, UUID.fromString(json.asText()));
                    } catch (IllegalArgumentException e) {
                        attr.set(to, null);
                    }
                else if (aType.isEnum()) {
                    Object[] cons = aType.getEnumConstants();
                    int ord = json.asInt(0);
                    Object c = cons.length > 0 ? cons[0] : null;
                    if (ord >= 0 && ord < cons.length) c = cons[ord];
                    attr.set(to, c);
                } else if (aType == Date.class) {
                    try {
                        attr.set(to, new Date(json.asLong(0)));
                    } catch (IllegalArgumentException e) {
                        attr.set(to, null);
                    }
                } else if (Map.class.isAssignableFrom(aType)) {

                    HashMap<String, Object> map = new HashMap<>();
                    for (Iterator<String> iter = json.fieldNames(); iter.hasNext(); ) {
                        String n = iter.next();
                        map.put(n, MJson.getValue(json.get(name), helper));
                    }
                    attr.set(to, map);

                } else if (Collection.class.isAssignableFrom(aType) && json instanceof ArrayNode) {
                    LinkedList<Object> list = new LinkedList<>();
                    ArrayNode array = (ArrayNode) json;
                    for (JsonNode a : array) {
                        list.add(MJson.getValue(a, helper));
                    }
                } else if (aType == String[].class) {
                    try {
                        LinkedList<String> l = new LinkedList<String>();
                        for (JsonNode i : json) {
                            l.add(i.asText());
                        }
                        attr.set(to, l.toArray(new String[l.size()]));
                    } catch (IllegalArgumentException e) {
                        attr.set(to, null);
                    }
                } else if (aType.isArray()) {
                    try {
                        LinkedList<Object> l = new LinkedList<Object>();
                        for (JsonNode i : json) {
                            Object obj = jsonToPojo(i, aType, helper);
                            l.add(obj);
                        }
                        attr.set(to, l.toArray((Object[]) Array.newInstance(aType, l.size())));
                    } catch (IllegalArgumentException e) {
                        attr.set(to, null);
                    }
                } else {
                    Object obj = jsonToPojo(json, aType, helper);
                    attr.set(to, obj);
                }
            } catch (Throwable t) {
                helper.log("ERROR " + name, t);
                throw new NotSupportedException(name, t);
            }
        }
        return to;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonNode pojoToJson(Object from, TransformHelper helper) throws NotSupportedException {

        ObjectNode to = MJson.createObjectNode();

        if (from instanceof Object[]) {
            // it's an array
            ArrayNode array = to.arrayNode();
            to.set("array", array);
            for (Object i : (Object[]) from) {
                JsonNode o = pojoToJson(i, helper.incLevel());
                array.add(o);
            }
            helper.decLevel();
            return to;
        }

        if (from instanceof Collection) {

            Collection<?> obj = (Collection<?>) from;
            ArrayNode array = to.arrayNode();
            to.set("_collection", array);
            for (Object o : obj) {
                JsonNode item = pojoToJson(o, helper.incLevel());
                array.add(item);
            }
            helper.decLevel();
            return to;
        }

        if (from instanceof Map) {
            ObjectNode obj = to.objectNode();
            to.set("_map", obj);
            for (Map.Entry<String, Object> e : ((Map<String, Object>) from).entrySet()) {
                JsonNode on = pojoToJson(e.getValue(), helper);
                obj.set(String.valueOf(e.getKey()), on);
            }
            helper.decLevel();
            return to;
        }

        PojoModel model = helper.createPojoModel(from);
        for (PojoAttribute<?> attr : model) {
            try {
                Object value = attr.get(from);
                String name = attr.getName();
                if (value == null) to.put(name, (String) null);
                else if (value instanceof Boolean) to.put(name, (boolean) value);
                else if (value instanceof Integer) to.put(name, (int) value);
                else if (value instanceof String) to.put(name, (String) value);
                else if (value.getClass().isEnum()) {
                    to.put(name, ((Enum<?>) value).ordinal());
                    to.put(name + "_", ((Enum<?>) value).name());
                } else if (value instanceof Date) to.put(name, ((Date) value).getTime());
                else if (value instanceof String[]) {
                    ArrayNode array = to.arrayNode();
                    to.set(name, array);
                    for (String i : (String[]) value) array.add(i);
                } else if (value instanceof Object[]) {
                    ArrayNode array = to.arrayNode();
                    to.set(name, array);
                    for (Object i : (Object[]) value) {
                        JsonNode o = pojoToJson(i, helper.incLevel());
                        array.add(o);
                    }
                } else if (value instanceof Map) {
                    ObjectNode obj = to.objectNode();
                    to.set(name, obj);
                    for (Map.Entry<String, Object> e : ((Map<String, Object>) value).entrySet())
                        obj.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
                } else if (value instanceof Collection) {
                    Collection<?> obj = (Collection<?>) value;
                    ArrayNode array = to.arrayNode();
                    to.set(name, array);
                    for (Object o : obj) {
                        JsonNode item = pojoToJson(o, helper.incLevel());
                        array.add(item);
                    }

                } else if (value instanceof Class) {
                    to.put(attr.getName(), ((Class<?>) value).getSimpleName());
                } else if (value instanceof UUID) {
                    to.put(attr.getName(), String.valueOf(value));
                } else {
                    if (!helper.checkLevel()) {
                        throw new NotSupportedException(
                                "too deep:"
                                        + attr.getName()
                                        + " "
                                        + value.getClass().getSimpleName());
                    }
                    JsonNode sub = pojoToJson(value, helper.incLevel());
                    to.set(attr.getName(), sub);
                }
            } catch (Throwable t) {
                helper.log(null, t);
                throw new NotSupportedException(t);
            }
        }

        return to;
    }

}
