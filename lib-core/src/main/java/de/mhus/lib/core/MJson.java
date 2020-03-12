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
package de.mhus.lib.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import de.mhus.lib.core.json.SerializerTransformer;
import de.mhus.lib.core.json.TransformHelper;
import de.mhus.lib.core.json.TransformStrategy;
import de.mhus.lib.core.lang.NullValue;
import de.mhus.lib.errors.MaxDepthReached;

public class MJson {

    public static final TransformStrategy DEFAULT_STRATEGY = new SerializerTransformer();
    public static final TransformHelper DEFAULT_HELPER = new TransformHelper();
    private static ObjectMapper mapper = new ObjectMapper();
    private static JsonFactory factory = new JsonFactory();

    public static void save(JsonNode json, File file)
            throws JsonGenerationException, JsonMappingException, IOException {
        FileOutputStream os = new FileOutputStream(file);
        save(json, os);
        os.close();
    }

    public static void save(JsonNode json, Writer w)
            throws JsonGenerationException, JsonMappingException, IOException {
        mapper.writeValue(w, json);
    }

    public static void save(JsonNode json, OutputStream w)
            throws JsonGenerationException, JsonMappingException, IOException {
        mapper.writeValue(w, json);
    }

    public static JsonNode load(File file) throws JsonProcessingException, IOException {
        FileInputStream is = new FileInputStream(file);
        try {
            return load(is);
        } finally {
            is.close();
        }
    }

    public static JsonNode load(InputStream r) throws JsonProcessingException, IOException {
        JsonParser parser = factory.createJsonParser(r);
        JsonNode in = mapper.readTree(parser);
        return in;
    }

    public static JsonNode load(String in) throws JsonProcessingException, IOException {
        JsonNode actualObj = mapper.readTree(in);
        return actualObj;
    }

    public static JsonNode load(Reader r) throws JsonProcessingException, IOException {
        JsonParser parser = factory.createJsonParser(r);
        JsonNode in = mapper.readTree(parser);
        return in;
    }

    public static void write(Object value, OutputStream out)
            throws JsonGenerationException, JsonMappingException, IOException {
        mapper.writeValue(out, value);
    }

    public static void write(Object value, Writer out)
            throws JsonGenerationException, JsonMappingException, IOException {
        mapper.writeValue(out, value);
    }

    public static String write(Object value)
            throws JsonGenerationException, JsonMappingException, IOException {
        return mapper.writeValueAsString(value);
    }

    public static <T> T read(InputStream r, Class<T> type)
            throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(r, type);
    }

    public static <T> T read(Reader r, Class<T> type)
            throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(r, type);
    }

    @SuppressWarnings("unchecked")
    public static <T> T read(Reader r, T def) {
        try {
            return (T) mapper.readValue(r, def.getClass());
        } catch (Exception e) {
            return def;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T read(InputStream r, T def) {
        try {
            return (T) mapper.readValue(r, def.getClass());
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * locate and return a json node inside a structure.
     *
     * @param parent
     * @param path slash separated path
     * @return requested node
     */
    public static JsonNode getByPath(JsonNode parent, String path) {
        if (path == null || parent == null) return null;
        JsonNode cur = parent;
        for (String part : path.split("/")) {
            if (MString.isSet(part)) cur = cur.get(part);
            if (cur == null) return null;
        }
        return cur;
    }

    /**
     * Search a node by path and return the value of the node.
     *
     * @param parent
     * @param path see getByPath
     * @param def
     * @return the value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(JsonNode parent, String path, T def) {
        Object out = getValue(parent, path);
        if (out == null) return def;
        return (T) MCast.toType(out, def.getClass(), def);
    }

    public static Object getValue(JsonNode parent, String path) {
        JsonNode node = getByPath(parent, path);
        return getValue(node);
    }

    public static Object getValue(JsonNode node) {
        if (node == null) return null;
        return getValue(node, (TransformHelper) null);
    }

    public static void setValues(ObjectNode node, Map<?, ?> map) {
        setValues(node, map, 0);
    }

    private static void setValues(ObjectNode node, Map<?, ?> map, int level) {
        if (level > MConstants.MAX_DEPTH_LEVEL) throw new MaxDepthReached();
        level++;
        if (map == null) return;
        for (Map.Entry<?, ?> e : map.entrySet())
            setValue(node, String.valueOf(e.getKey()), e.getValue(), level);
    }

    public static void setValue(ObjectNode node, String name, Object value) {
        setValue(node, name, value, 0);
    }

    private static void setValue(ObjectNode node, String name, Object value, int level) {
        if (level > MConstants.MAX_DEPTH_LEVEL) throw new MaxDepthReached();
        level++;
        if (value == null || value instanceof NullValue) {
            node.putNull(name);
            return;
        }
        if (value instanceof Boolean) {
            node.put(name, (Boolean) value);
            return;
        }
        if (value instanceof Double) {
            node.put(name, (Double) value);
            return;
        }
        if (value instanceof BigDecimal) {
            node.put(name, (BigDecimal) value);
            return;
        }
        if (value instanceof Float) {
            node.put(name, (Float) value);
            return;
        }
        if (value instanceof Integer) {
            node.put(name, (Integer) value);
            return;
        }
        if (value instanceof Long) {
            node.put(name, (Long) value);
            return;
        }
        if (value instanceof byte[]) {
            node.put(name, (byte[]) value);
            return;
        }
        if (value instanceof Date) {
            node.put(name, ((Date) value).getTime());
            return;
        }
        if (value instanceof JsonNode) {
            node.put(name, (JsonNode) value);
            return;
        }
        if (value.getClass().isArray()) {
            ArrayNode array = node.arrayNode();
            node.put(name, array);
            setValues(array, (Object[]) value, level);
            return;
        }
        if (value instanceof Collection<?>) {
            ArrayNode array = node.arrayNode();
            node.put(name, array);
            setValues(array, (Collection<?>) value, level);
            return;
        }
        if (value instanceof Map<?, ?>) {
            ObjectNode obj = node.objectNode();
            node.put(name, obj);
            setValues(obj, (Map<?, ?>) value);
            return;
        }
        node.put(name, value.toString());
    }

    public static void setValues(ArrayNode array, Object[] value) {
        setValues(array, value, 0);
    }

    private static void setValues(ArrayNode array, Object[] value, int level) {
        if (level > MConstants.MAX_DEPTH_LEVEL) throw new MaxDepthReached();
        level++;
        for (Object obj : value) {
            if (obj == null || obj instanceof NullValue) {
                array.addNull();
            } else if (obj instanceof Boolean) {
                array.add((Boolean) obj);
            } else if (obj instanceof Double) {
                array.add((Double) obj);
            } else if (obj instanceof BigDecimal) {
                array.add((BigDecimal) obj);
            } else if (obj instanceof Float) {
                array.add((Float) obj);
            } else if (obj instanceof Integer) {
                array.add((Integer) obj);
            } else if (obj instanceof Long) {
                array.add((Long) obj);
            } else if (obj instanceof byte[]) {
                array.add((byte[]) obj);
            } else if (obj instanceof Date) {
                array.add(((Date) obj).getTime());
            } else if (obj instanceof JsonNode) {
                array.add((JsonNode) obj);
            } else if (obj.getClass().isArray()) {
                ArrayNode array2 = array.addArray();
                setValues(array2, (Object[]) obj, level);
            } else if (obj instanceof Collection<?>) {
                ArrayNode array2 = array.addArray();
                setValues(array2, (Collection<?>) obj, level);
            } else if (obj instanceof Map<?, ?>) {
                ObjectNode objNode = array.addObject();
                setValues(objNode, (Map<?, ?>) obj, level);
            } else {
                // TODO load via pojo?
            }
        }
    }

    public static void setValues(ArrayNode array, Collection<?> value) {
        setValues(array, value, 0);
    }

    private static void setValues(ArrayNode array, Collection<?> value, int level) {
        if (level > MConstants.MAX_DEPTH_LEVEL) throw new MaxDepthReached();
        level++;
        for (Object obj : value) {
            if (obj == null || obj instanceof NullValue) {
                array.addNull();
            } else if (obj instanceof Boolean) {
                array.add((Boolean) obj);
            } else if (obj instanceof Double) {
                array.add((Double) obj);
            } else if (obj instanceof BigDecimal) {
                array.add((BigDecimal) obj);
            } else if (obj instanceof Float) {
                array.add((Float) obj);
            } else if (obj instanceof Integer) {
                array.add((Integer) obj);
            } else if (obj instanceof Long) {
                array.add((Long) obj);
            } else if (obj instanceof byte[]) {
                array.add((byte[]) obj);
            } else if (obj instanceof Date) {
                array.add(((Date) obj).getTime());
            } else if (obj instanceof JsonNode) {
                array.add((JsonNode) obj);
            } else if (obj.getClass().isArray()) {
                ArrayNode array2 = array.addArray();
                setValues(array2, (Object[]) obj, level);
            } else if (obj instanceof Collection<?>) {
                ArrayNode array2 = array.addArray();
                setValues(array2, (Collection<?>) obj, level);
            } else if (obj instanceof Map<?, ?>) {
                ObjectNode objNode = array.addObject();
                setValues(objNode, (Map<?, ?>) obj, level);
            } else {
                // TODO load via pojo?
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static Object getValue(JsonNode node, TransformHelper helper) {
        Object out = null;
        if (node == null) return null;
        try {
            if (node.isNull()) out = null;
            else if (node.isTextual()) out = node.getValueAsText();
            else if (node.isBigDecimal()) out = node.getDecimalValue();
            else if (node.isBigInteger()) out = node.getBigIntegerValue();
            else if (node.isBinary()) out = node.getBinaryValue();
            else if (node.isBoolean()) out = node.getBooleanValue();
            else if (node.isDouble()) out = node.getDoubleValue();
            else if (node.isInt()) out = node.getIntValue();
            else if (node.isLong()) out = node.getLongValue();
            else if (node.isNumber()) out = node.getNumberValue();
            else if (node.isArray()) {
                LinkedList<Object> l = new LinkedList<>();
                for (JsonNode n : node) {
                    l.add(getValue(n, helper));
                }
                out = l;
            } else if (node.isObject()) {
                HashMap<String, Object> m = new HashMap<>();
                for (Iterator<String> i = node.getFieldNames(); i.hasNext(); ) {
                    String name = i.next();
                    m.put(name, getValue(node.get(name), helper));
                }
            }
        } catch (IOException e) {
        }
        return out;
    }
    /**
     * Search a node and returns the text value.
     *
     * @param parent
     * @param path
     * @param def
     * @return the value
     */
    public static String getText(JsonNode parent, String path, String def) {
        JsonNode node = getByPath(parent, path);
        if (node == null) return def;
        if (node.isTextual()) {
            String out = null;
            out = node.getTextValue();
            if (out == null) out = def;
            return out;
        }
        if (node.isBoolean()) {
            boolean out = node.getBooleanValue();
            return MCast.toString(out);
        }
        if (node.isInt()) {
            int out = node.getIntValue();
            return MCast.toString(out);
        }
        if (node.isDouble()) {
            double out = node.getDoubleValue();
            return MCast.toString(out);
        }
        if (node.isLong()) {
            long out = node.getLongValue();
            return MCast.toString(out);
        }
        if (node.isBigInteger()) {
            BigInteger out = node.getBigIntegerValue();
            return MCast.toString(out);
        }
        if (node.isBigDecimal()) {
            BigDecimal out = node.getDecimalValue();
            return MCast.toString(out);
        }
        return node.toString();
    }

    /*
     * Transform a object via pojo framework to a json structure.
     *
     * @param from
     * @param to
     * @return
     * @throws IOException
     */
    //	public static void pojoToJson(Object from, ObjectNode to) throws IOException {
    //		pojoToJson(from, to, null);
    //	}

    public static JsonNode pojoToJson(Object from) {
        return pojoToJson(from, null);
    }

    //	public static void pojoToJson(Object from, ObjectNode to, TransformHelper helper) throws
    // IOException {
    //		if (helper == null) helper = DEFAULT_HELPER;
    //		JsonNode x = pojoToJson( mapper.writeValueAsString(from), helper );
    //		to.put("_object", x);
    //		helper.postToJson(from, to);
    //	}

    /**
     * Transform a json structure into an object
     *
     * @param from
     * @return the object
     */
    public static Object jsonToPojo(JsonNode from) {
        return jsonToPojo(from, null, null);
    }

    public static Object jsonToPojo(JsonNode from, TransformHelper helper) {
        return jsonToPojo(from, null, helper);
    }

    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    public static JsonNode pojoToJson(Object from, TransformHelper helper) {
        if (helper == null) helper = DEFAULT_HELPER;
        JsonNode to = helper.getStrategy().pojoToJson(from, helper);
        helper.postToJson(from, to);
        return to;
    }

    public static Object jsonToPojo(JsonNode from, Class<?> type, TransformHelper helper) {
        if (helper == null) helper = DEFAULT_HELPER;
        Object to = helper.getStrategy().jsonToPojo(from, type, helper);
        helper.postToPojo(from, to);
        return to;
    }

    public static ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static String encode(String in) {
        if (in == null) return null;
        if (in.indexOf('\\') < 0 && in.indexOf('"') < 0) return in;

        in = in.replace("\\", "\\\\");
        in = in.replace("\"", "\\\"");
        //		in = in.replaceAll("\\\\\\\\", "\\\\");
        //		in = in.replaceAll("\"", "\\\"");

        return in;
    }

    public static String encodeValue(Object in) {
        if (in == null) return "null";
        if (in instanceof Integer
                || in instanceof Long
                || in instanceof Byte
                || in instanceof Short
                || in instanceof Double
                || in instanceof Float) return String.valueOf(in).replace(',', '.');
        if (in instanceof Date) return String.valueOf(((Date) in).getTime());
        return '"' + encode(String.valueOf(in)) + '"';
    }

    public static String toString(JsonNode to)
            throws JsonGenerationException, JsonMappingException, IOException {
        return mapper.writeValueAsString(to);
    }
}
