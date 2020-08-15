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

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.errors.NotSupportedException;

public class SerializerTransformer extends TransformStrategy {

    @SuppressWarnings("unchecked")
    @Override
    public Object jsonToPojo(JsonNode from, Class<?> typex, TransformHelper helper)
            throws NotSupportedException {

        if (from instanceof TextNode) return ((TextNode) from).asText();
        if (from instanceof ArrayNode) {
            ArrayNode node = (ArrayNode) from;
            LinkedList<Object> list = new LinkedList<>();
            JsonNode first = null;
            for (JsonNode item : node) {
                if (first == null) first = item;
                else {
                    list.add(jsonToPojo(item, null, helper));
                }
            }
            String aclazz = first.get("_arraytype").asText();
            Object array = null;
            try {
                array = helper.createArray(list.size(), helper.getType(aclazz));
                for (int i = 0; i < list.size(); i++) Array.set(array, i, list.get(i));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return array;
        }

        if (MJson.getValue(from, "_null", false)) return null; // return null is ok !

        if (from instanceof IntNode) return ((IntNode) from).asInt();
        if (from instanceof BooleanNode) return ((BooleanNode) from).asBoolean();
        if (from instanceof DoubleNode) return ((DoubleNode) from).asDouble();
        if (from instanceof NullNode) return null;

        if (!(from instanceof ObjectNode)) throw new NotSupportedException("node type is unknown");

        String clazz = MJson.getValue(from, "_type", "");
        String special = MJson.getValue(from, "_special", "");
        if ("json".equals(special)) {
            ((ObjectNode) from).remove("_special");
            return from;
        }

        JsonNode val = MJson.getByPath(from, "_value");

        if (val != null) {
            try {
                switch (clazz) {
                    case "java.lang.String":
                        return val.asText();
                    case "java.lang.Boolean":
                        return val.asBoolean();
                    case "java.lang.Byte":
                        return val.binaryValue()[0];
                    case "java.lang.Integer":
                        return val.asInt();
                    case "java.lang.Short":
                        return val.numberValue().shortValue();
                    case "java.lang.Double":
                        return val.asDouble();
                    case "java.lang.Float":
                        return val.numberValue().floatValue();
                    case "java.lang.Long":
                        return val.numberValue().longValue();
                    case "java.lang.Character":
                        return (char) val.asInt();
                }

            } catch (IOException e) {
                log().d(e);
                throw new NotSupportedException("exception", e);
            }
        }

        if (special.equals("map")) {
            ObjectNode map = (ObjectNode) from.get("_map");
            Map<Object, Object> out = null;
            try {
                out = (Map<Object, Object>) helper.createObject(helper.getType(clazz));
            } catch (Throwable e) {
                out = new HashMap<>();
            }
            Iterator<Entry<String, JsonNode>> fieldIter = map.fields();
            while (fieldIter.hasNext()) {
                Entry<String, JsonNode> field = fieldIter.next();
                if (!field.getKey().startsWith("_")) {
                    out.put(field.getKey(), jsonToPojo(field.getValue(), null, helper));
                }
            }
            return out;
        } else if (special.equals("collection")) {
            ArrayNode array = (ArrayNode) from.get("_array");
            Object[] a = (Object[]) jsonToPojo(array, null, helper);
            Collection<Object> out = null;
            try {
                out = (Collection<Object>) helper.createObject(helper.getType(clazz));
            } catch (Throwable e) {
                out = new LinkedList<>();
            }
            for (Object o : a) out.add(o);
            return out;
        } else {
            try {
                Object to = helper.createObject(helper.getType(clazz));
                PojoModel model = helper.createPojoModel(to);
                for (PojoAttribute<Object> attr : model) {
                    String name = attr.getName();
                    Class<?> type = attr.getType();
                    Object value = jsonToPojo(from.get(name), null, helper);
                    if (value == null) attr.set(to, null, true);
                    else if (type.isInstance(value)) attr.set(to, value, true);
                    else System.out.println("Can't set ...");
                }
                return to;
            } catch (Throwable t) {
                log().d(t);
                throw new NotSupportedException("exception", t);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public JsonNode pojoToJson(Object from, TransformHelper helper) throws NotSupportedException {

        if (from == null) {
            ObjectNode first = MJson.createObjectNode();
            first.put("_null", true);
            return first;
        }

        if (from instanceof ObjectNode) {
            ((ObjectNode) from).put("_special", "json");
            return (ObjectNode) from;
        }

        if (from.getClass().isArray()) {
            ArrayNode out = MJson.createArrayNode();

            ObjectNode first = MJson.createObjectNode();
            first.put("_arraytype", from.getClass().getCanonicalName());
            out.add(first);
            int l = Array.getLength(from);
            for (int i = 0; i < l; i++) {
                Object o = Array.get(from, i);
                JsonNode on = pojoToJson(o, helper.incLevel());
                out.add(on);
            }

            helper.decLevel();
            return out;
        }

        {
            ObjectNode out = MJson.createObjectNode();

            if (from instanceof String
                    || from instanceof Integer
                    || from instanceof Boolean
                    || from instanceof Short
                    || from instanceof Long
                    || from instanceof Double
                    || from instanceof Float
                    || from instanceof Character
                    || from instanceof Byte) {
                out.put("_type", from.getClass().getCanonicalName());
                putPojoValue(out, "_value", from, helper);
            } else if (from instanceof Map) {
                out.put("_type", from.getClass().getCanonicalName());
                out.put("_special", "map");
                @SuppressWarnings({"rawtypes"})
                Map<Object, Object> map = (Map) from;
                ObjectNode x = out.objectNode();
                out.set("_map", x);
                for (Map.Entry<Object, Object> en : map.entrySet()) {
                    putPojoValue(x, String.valueOf(en.getKey()), en.getValue(), helper);
                }

            } else if (from instanceof Collection) {
                out.put("_type", from.getClass().getCanonicalName());
                out.put("_special", "collection");
                @SuppressWarnings({"rawtypes"})
                Collection<Object> col = (Collection) from;
                out.set("_array", pojoToJson(col.toArray()));
            } else {
                out.put("_type", from.getClass().getCanonicalName());

                PojoModel model = helper.createPojoModel(from);
                for (PojoAttribute<Object> attr : model) {
                    String name = attr.getName();
                    try {
                        putPojoValue(out, name, attr.get(from), helper);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            helper.decLevel();
            return out;
        }
    }

    @SuppressWarnings("deprecation")
    protected void putPojoValue(ObjectNode out, String name, Object value, TransformHelper helper) {
        if (value == null) out.putNull(name);
        else if (value instanceof Byte) out.put(name, new byte[] {(Byte) value});
        else if (value instanceof String) out.put(name, (String) value);
        else if (value instanceof Long) out.put(name, (Long) value);
        else if (value instanceof Integer) out.put(name, (Integer) value);
        else if (value instanceof Double) out.put(name, (Double) value);
        else if (value instanceof Short) out.put(name, (Short) value);
        else if (value instanceof Float) out.put(name, (Double) value);
        else if (value instanceof Character) out.put(name, (Character) value);
        else if (value instanceof Boolean) out.put(name, (Boolean) value);
        else out.put(name, pojoToJson(value, helper.incLevel()));
    }
}
