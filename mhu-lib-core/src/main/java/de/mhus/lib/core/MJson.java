package de.mhus.lib.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
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
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;

/**
 * <p>MJson class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class MJson {
	
	/** Constant <code>DEFAULT_STRATEGY</code> */
	public static final TransformStrategy DEFAULT_STRATEGY = new SerializerTransformer();
	/** Constant <code>DEFAULT_HELPER</code> */
	public static final TransformHelper DEFAULT_HELPER = new TransformHelper();
	private static ObjectMapper mapper = new ObjectMapper();
	private static JsonFactory factory = new JsonFactory();

	
	/**
	 * <p>save.</p>
	 *
	 * @param json a {@link org.codehaus.jackson.JsonNode} object.
	 * @param w a {@link java.io.Writer} object.
	 * @throws org.codehaus.jackson.JsonGenerationException if any.
	 * @throws org.codehaus.jackson.map.JsonMappingException if any.
	 * @throws java.io.IOException if any.
	 */
	public static void save(JsonNode json, Writer w) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(w,json);
	}
	
	/**
	 * <p>save.</p>
	 *
	 * @param json a {@link org.codehaus.jackson.JsonNode} object.
	 * @param w a {@link java.io.OutputStream} object.
	 * @throws org.codehaus.jackson.JsonGenerationException if any.
	 * @throws org.codehaus.jackson.map.JsonMappingException if any.
	 * @throws java.io.IOException if any.
	 */
	public static void save(JsonNode json, OutputStream w) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(w,json);
	}
	
	/**
	 * <p>load.</p>
	 *
	 * @param r a {@link java.io.InputStream} object.
	 * @return a {@link org.codehaus.jackson.JsonNode} object.
	 * @throws org.codehaus.jackson.JsonProcessingException if any.
	 * @throws java.io.IOException if any.
	 */
	public static JsonNode load(InputStream r) throws JsonProcessingException, IOException {
		JsonParser parser = factory.createJsonParser(r);
		JsonNode in = mapper.readTree(parser);
		return in;
	}
	
	/**
	 * <p>load.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 * @return a {@link org.codehaus.jackson.JsonNode} object.
	 * @throws org.codehaus.jackson.JsonProcessingException if any.
	 * @throws java.io.IOException if any.
	 * @since 3.2.9
	 */
	public static JsonNode load(String in) throws JsonProcessingException, IOException {
		JsonNode actualObj = mapper.readTree(in);
		return actualObj;
	}
	
	/**
	 * <p>load.</p>
	 *
	 * @param r a {@link java.io.Reader} object.
	 * @return a {@link org.codehaus.jackson.JsonNode} object.
	 * @throws org.codehaus.jackson.JsonProcessingException if any.
	 * @throws java.io.IOException if any.
	 */
	public static JsonNode load(Reader r) throws JsonProcessingException, IOException {
		JsonParser parser = factory.createJsonParser(r);
		JsonNode in = mapper.readTree(parser);
		return in;
	}
	
	/**
	 * <p>write.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 * @param out a {@link java.io.OutputStream} object.
	 * @throws org.codehaus.jackson.JsonGenerationException if any.
	 * @throws org.codehaus.jackson.map.JsonMappingException if any.
	 * @throws java.io.IOException if any.
	 */
	public static void write(Object value, OutputStream out) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(out, value);
	}
	
	/**
	 * <p>write.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 * @param out a {@link java.io.Writer} object.
	 * @throws org.codehaus.jackson.JsonGenerationException if any.
	 * @throws org.codehaus.jackson.map.JsonMappingException if any.
	 * @throws java.io.IOException if any.
	 */
	public static void write(Object value, Writer out) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(out, value);
	}
	
	/**
	 * <p>write.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 * @throws org.codehaus.jackson.JsonGenerationException if any.
	 * @throws org.codehaus.jackson.map.JsonMappingException if any.
	 * @throws java.io.IOException if any.
	 * @since 3.2.9
	 */
	public static String write(Object value) throws JsonGenerationException, JsonMappingException, IOException {
		return mapper.writeValueAsString(value);
	}
	
	/**
	 * <p>read.</p>
	 *
	 * @param r a {@link java.io.InputStream} object.
	 * @param type a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 * @throws org.codehaus.jackson.JsonParseException if any.
	 * @throws org.codehaus.jackson.map.JsonMappingException if any.
	 * @throws java.io.IOException if any.
	 */
	public static <T> T read(InputStream r, Class<T> type) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(r, type);
	}
	
	/**
	 * <p>read.</p>
	 *
	 * @param r a {@link java.io.Reader} object.
	 * @param type a {@link java.lang.Class} object.
	 * @param <T> a T object.
	 * @return a T object.
	 * @throws org.codehaus.jackson.JsonParseException if any.
	 * @throws org.codehaus.jackson.map.JsonMappingException if any.
	 * @throws java.io.IOException if any.
	 */
	public static <T> T read(Reader r, Class<T> type) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(r, type);
	}

	/**
	 * <p>read.</p>
	 *
	 * @param r a {@link java.io.Reader} object.
	 * @param def a T object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T read(Reader r, T def) {
		try {
			return (T) mapper.readValue(r, def.getClass());
		} catch (Exception e) {
			return def;
		}
	}
	
	/**
	 * <p>read.</p>
	 *
	 * @param r a {@link java.io.InputStream} object.
	 * @param def a T object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
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
	 * @param parent a {@link org.codehaus.jackson.JsonNode} object.
	 * @param path slash separated path
	 * @return a {@link org.codehaus.jackson.JsonNode} object.
	 */
	public static JsonNode getByPath(JsonNode parent, String path) {
		if (path == null || parent == null) return null;
		JsonNode cur = parent;
		for (String part : path.split("/")) {
			if (MString.isSet(part))
				cur = cur.get(part);
			if (cur == null) return null;
		}
		return cur;
	}
	
	/**
	 * Search a node by path and return the value of the node.
	 *
	 * @param parent a {@link org.codehaus.jackson.JsonNode} object.
	 * @param path see getByPath
	 * @param def a T object.
	 * @param <T> a T object.
	 * @return a T object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValue(JsonNode parent, String path, T def ) {
		Object out = getValue(parent, path);
		if (out == null) return def;
		return (T)MCast.toType(out, def.getClass(), def);
	}

	/**
	 * <p>getValue.</p>
	 *
	 * @param parent a {@link org.codehaus.jackson.JsonNode} object.
	 * @param path a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public static Object getValue(JsonNode parent, String path) {
		JsonNode node = getByPath(parent, path);
		return getValue(node);
	}
	
	/**
	 * <p>getValue.</p>
	 *
	 * @param node a {@link org.codehaus.jackson.JsonNode} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public static Object getValue(JsonNode node) {
		if (node == null) return null;
		return getValue(node, (TransformHelper)null);
	}
	
	/**
	 * <p>getValue.</p>
	 *
	 * @param node a {@link org.codehaus.jackson.JsonNode} object.
	 * @param helper a {@link de.mhus.lib.core.json.TransformHelper} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public static Object getValue(JsonNode node, TransformHelper helper) {
		Object out = null;
		if (node == null) return null;
		try {
			if (node.isNull())
				out = null;
			else if (node.isTextual())
				out = node.getValueAsText();
			else if (node.isBigDecimal())
				out = node.getDecimalValue();
			else if (node.isBigInteger())
				out = node.getBigIntegerValue();
			else if (node.isBinary())
				out = node.getBinaryValue();
			else if (node.isBoolean())
				out = node.getBooleanValue();
			else if (node.isDouble())
				out = node.getDoubleValue();
			else if (node.isInt())
				out = node.getIntValue();
			else if (node.isLong())
				out = node.getLongValue();
			else if (node.isNumber())
				out = node.getNumberValue();
			else if (node.isArray()) {
				LinkedList<Object> l = new LinkedList<>();
				for (JsonNode n : node) {
					l.add(getValue(n,helper));
				}
				out = l;
			}
			else if (node.isObject()) {
				HashMap<String, Object> m = new HashMap<>();
				for (Iterator<String> i = node.getFieldNames(); i.hasNext();) {
					String name = i.next();
					m.put(name, getValue(node.get(name), helper));
				}
			}
		} catch (IOException e) {}
		return out;
	}
	/**
	 * Search a node and returns the text value.
	 *
	 * @param parent a {@link org.codehaus.jackson.JsonNode} object.
	 * @param path a {@link java.lang.String} object.
	 * @param def a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public static String getText(JsonNode parent, String path, String def ) {
		JsonNode node = getByPath(parent, path);
		if (node == null) return def;
		String out = null;
		out = node.getTextValue();
		if (out == null) out = def;
		return out;
	}
	
	/**
	 * Transform a object via pojo framework to a json structure.
	 *
	 * @param from a {@link java.lang.Object} object.
	 * @return a {@link org.codehaus.jackson.JsonNode} object.
	 */
//	public static void pojoToJson(Object from, ObjectNode to) throws IOException {
//		pojoToJson(from, to, null);
//	}
	public static JsonNode pojoToJson(Object from) {
		return pojoToJson(from, null);
	}
	
//	public static void pojoToJson(Object from, ObjectNode to, TransformHelper helper) throws IOException {
//		if (helper == null) helper = DEFAULT_HELPER;
//		JsonNode x = pojoToJson( mapper.writeValueAsString(from), helper );
//		to.put("_object", x);
//		helper.postToJson(from, to);
//	}
		
	/**
	 * <p>propertiesToPojo.</p>
	 *
	 * @param from a {@link java.util.Map} object.
	 * @param to a {@link java.lang.Object} object.
	 * @param helper a {@link de.mhus.lib.core.json.TransformHelper} object.
	 * @throws java.io.IOException if any.
	 */
	@SuppressWarnings("unchecked")
	public static void propertiesToPojo(Map<String,String> from, Object to, TransformHelper helper) throws IOException {
		PojoModel model = helper.createPojoModel(from);
		for (PojoAttribute<Object> attr : model) {
			String name = attr.getName();
			String value = from.get(name);
			if (value != null) {
				attr.set(to, value);
			}
		}
	}

	/**
	 * Transform a json structure into an object
	 *
	 * @param from a {@link org.codehaus.jackson.JsonNode} object.
	 * @return a {@link java.lang.Object} object.
	 * @since 3.2.9
	 */
	public static Object jsonToPojo(JsonNode from) {
		return jsonToPojo(from, null, null);
	}

	/**
	 * <p>jsonToPojo.</p>
	 *
	 * @param from a {@link org.codehaus.jackson.JsonNode} object.
	 * @param helper a {@link de.mhus.lib.core.json.TransformHelper} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public static Object jsonToPojo(JsonNode from, TransformHelper helper) {
		return jsonToPojo(from, null, helper);
	}

	/**
	 * <p>createObjectNode.</p>
	 *
	 * @return a {@link org.codehaus.jackson.node.ObjectNode} object.
	 */
	public static ObjectNode createObjectNode() {
		return mapper.createObjectNode();
	}
	
	/**
	 * <p>pojoToJson.</p>
	 *
	 * @param from a {@link java.lang.Object} object.
	 * @param helper a {@link de.mhus.lib.core.json.TransformHelper} object.
	 * @return a {@link org.codehaus.jackson.JsonNode} object.
	 */
	public static JsonNode pojoToJson(Object from, TransformHelper helper) {
		if (helper == null) helper = DEFAULT_HELPER;
		JsonNode to = helper.getStrategy().pojoToJson(from, helper);
		helper.postToJson(from, to);
		return to;
	}

	/**
	 * <p>jsonToPojo.</p>
	 *
	 * @param from a {@link org.codehaus.jackson.JsonNode} object.
	 * @param type a {@link java.lang.Class} object.
	 * @param helper a {@link de.mhus.lib.core.json.TransformHelper} object.
	 * @return a {@link java.lang.Object} object.
	 */
	public static Object jsonToPojo(JsonNode from, Class<?> type, TransformHelper helper) {
		if (helper == null) helper = DEFAULT_HELPER;
		Object to = helper.getStrategy().jsonToPojo(from, type, helper);
		helper.postToPojo(from, to);
		return to;
	}

	/**
	 * <p>createArrayNode.</p>
	 *
	 * @return a {@link org.codehaus.jackson.node.ArrayNode} object.
	 * @since 3.2.9
	 */
	public static ArrayNode createArrayNode() {
		return mapper.createArrayNode();
	}

	/**
	 * <p>Getter for the field <code>mapper</code>.</p>
	 *
	 * @return a {@link org.codehaus.jackson.map.ObjectMapper} object.
	 * @since 3.2.9
	 */
	public static ObjectMapper getMapper() {
		return mapper;
	}
	
	/**
	 * <p>encode.</p>
	 *
	 * @param in a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 * @since 3.2.9
	 */
	public static String encode(String in) {
		if (in == null) return null;
		if (in.indexOf('\\') < 0 && in.indexOf('"') < 0) return in;
		
		in = in.replace("\\", "\\\\");
		in = in.replace("\"", "\\\"");
//		in = in.replaceAll("\\\\\\\\", "\\\\");
//		in = in.replaceAll("\"", "\\\"");
		
		return in;
	}

	/**
	 * <p>encodeValue.</p>
	 *
	 * @param in a {@link java.lang.Object} object.
	 * @return a {@link java.lang.String} object.
	 * @since 3.2.9
	 */
	public static String encodeValue(Object in) {
		if (in == null) return "null";
		if (in instanceof Integer || in instanceof Long || in instanceof Byte || in instanceof Short || in instanceof Double || in instanceof Float)
			return String.valueOf(in).replace(',', '.');
		if (in instanceof Date)
			return String.valueOf( ((Date)in).getTime() );
		return
				'"' + encode(String.valueOf(in)) + '"';
	}

	/**
	 * <p>toString.</p>
	 *
	 * @param to a {@link org.codehaus.jackson.JsonNode} object.
	 * @return a {@link java.lang.String} object.
	 * @throws org.codehaus.jackson.JsonGenerationException if any.
	 * @throws org.codehaus.jackson.map.JsonMappingException if any.
	 * @throws java.io.IOException if any.
	 * @since 3.2.9
	 */
	public static String toString(JsonNode to) throws JsonGenerationException, JsonMappingException, IOException {
		return mapper.writeValueAsString(to);
	}
	
}
