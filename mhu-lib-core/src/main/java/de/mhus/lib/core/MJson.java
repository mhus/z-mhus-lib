package de.mhus.lib.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Date;
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

public class MJson {
	
	public static final TransformStrategy DEFAULT_STRATEGY = new SerializerTransformer();
	public static final TransformHelper DEFAULT_HELPER = new TransformHelper();
	private static ObjectMapper mapper = new ObjectMapper();
	private static JsonFactory factory = new JsonFactory();

	
	public static void save(JsonNode json, Writer w) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(w,json);
	}
	
	public static void save(JsonNode json, OutputStream w) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(w,json);
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
	
	public static void write(Object value, OutputStream out) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(out, value);
	}
	
	public static void write(Object value, Writer out) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(out, value);
	}
	
	public static String write(Object value) throws JsonGenerationException, JsonMappingException, IOException {
		return mapper.writeValueAsString(value);
	}
	
	public static <T> T read(InputStream r, Class<T> type) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(r, type);
	}
	
	public static <T> T read(Reader r, Class<T> type) throws JsonParseException, JsonMappingException, IOException {
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
	 * @return
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
	 * @param parent
	 * @param path see getByPath
	 * @param def
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValue(JsonNode parent, String path, T def ) {
		Object out = getValue(parent, path);
		if (out == null) return def;
		return (T)MCast.toType(out, def.getClass(), def);
	}

	public static Object getValue(JsonNode parent, String path) {
		JsonNode node = getByPath(parent, path);
		return getValue(node);
	}
	
	public static Object getValue(JsonNode node) {
		if (node == null) return null;
		return getValue(node, (TransformHelper)null);
	}
	
	public static Object getValue(JsonNode node, TransformHelper helper) {
		Object out = null;
		if (node == null) return null;
		try {
			if (node.isTextual())
				out = node.getValueAsText();
			else if (node.isNull())
				out = null;
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
		} catch (IOException e) {}
		return out;
	}
	/**
	 * Search a node and returns the text value.
	 * @param parent
	 * @param path
	 * @param def
	 * @return
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
	
//	public static void pojoToJson(Object from, ObjectNode to, TransformHelper helper) throws IOException {
//		if (helper == null) helper = DEFAULT_HELPER;
//		JsonNode x = pojoToJson( mapper.writeValueAsString(from), helper );
//		to.put("_object", x);
//		helper.postToJson(from, to);
//	}
		
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
	 * @param from
	 * @param to
	 * @return 
	 * @throws IOException
	 * @throws IllegalAccessException 
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
	
//	@SuppressWarnings("unchecked")
//	public static Object jsonToPojo(JsonNode from, Object to, TransformHelper helper) throws IOException, IllegalAccessException {
//		if (helper == null) helper = DEFAULT_HELPER;
//		if (to == null) {
//			to = helper.createObject(from);
//		}
//		if (to == null) return null;
//		
//		if (from.size() == 1) {
//			String singleName = from.getFieldNames().next();
//			if ("_collection".equals(singleName)) {
//				LinkedList<Object> out = new LinkedList<>();
//				for (JsonNode n : from.get(0)) {
//					out.add(jsonToPojo(n, null, helper.incLevel()));
//				}
//				helper.decLevel();
//				return out;
//			} else
//			if ("_map".equals(singleName)) {
//				HashMap<Object, Object> out = new HashMap<>();
//				Iterator<String> nameIter = from.getFieldNames();
//				while(nameIter.hasNext()) {
//					String name = nameIter.next();
//					JsonNode on = from.get(name);
//					out.put(name, jsonToPojo(on, null, helper.incLevel()));
//				}
//				helper.decLevel();
//				return out;
//			}
//		}
//		
//		
//		PojoModel model = helper.createPojoModel(to);
//		for (PojoAttribute<Object> attr : model) {
//			String name = attr.getName();
//			Class<?> type = attr.getType();
//			JsonNode json = from.get(helper.getPrefix() + name);
//			
//			try {
//				if (json == null || !attr.canWrite() ) {
//					
//				} else
//				if (type == Boolean.class || type == boolean.class)
//					attr.set(to, json.getValueAsBoolean(false));
//				else
//				if (type == Integer.class || type == int.class)
//					attr.set(to, json.getValueAsInt(0));
//				else
//				if (type == String.class)
//					attr.set(to, json.getValueAsText());
//				else
//				if (type == UUID.class)
//					try {
//						attr.set(to, UUID.fromString(json.getValueAsText()));
//					} catch (IllegalArgumentException e) {
//						attr.set(to, null);
//					}
//				else
//				if (type.isEnum()) {
//					Object[] cons=type.getEnumConstants();
//					int ord = json.getValueAsInt(0);
//					Object c = cons.length > 0 ? cons[0] : null;
//					if (ord >=0 && ord < cons.length) c = cons[ord];
//					attr.set(to, c );
//				}
//				else
//				if (type == Date.class) {
//					try {
//						attr.set(to, new Date(json.getValueAsLong(0)) );
//					} catch (IllegalArgumentException e) {
//						attr.set(to, null);
//					}
//				}
//				else
//				if (Map.class.isAssignableFrom(type)) {
//
//					HashMap<String, Object> map = new HashMap<>();
//					for (Iterator<String> iter = json.getFieldNames(); iter.hasNext();) {
//						String n = iter.next();
//						map.put(n, getValue(json.get(name), helper) );
//					}
//					attr.set(to, map );
//
//				} else
//				if (Collection.class.isAssignableFrom(type) && json instanceof ArrayNode) {
//					LinkedList<Object> list = new LinkedList<>();
//					ArrayNode array = (ArrayNode)json;
//					for (JsonNode a : array) {
//						list.add( getValue(a, helper) );
//					}
//				} else
//				if (type == String[].class) {
//					try {
//						LinkedList<String> l = new LinkedList<String>();
//						for (JsonNode i : json) {
//							l.add(i.getValueAsText());
//						}
//						attr.set(to, l.toArray(new String[l.size()]));
//					} catch (IllegalArgumentException e) {
//						attr.set(to, null);
//					}
//				} 
//				else
//				if (type.isArray()) {
//					try {
//						LinkedList<Object> l = new LinkedList<Object>();
//						for (JsonNode i : json) {
//							Object obj = helper.createObject(type);
//							if (obj != null) {
//								jsonToPojo(i, obj, helper);
//								l.add(obj);
//							}
//						}
//						attr.set(to, l.toArray( (Object[])Array.newInstance(type, l.size()) ));
//					} catch (IllegalArgumentException e) {
//						attr.set(to, null);
//					}
//				}
//				else {
//					Object obj = helper.createObject(type);
//					if (obj != null)
//						jsonToPojo(json, obj, helper);
//					attr.set(to, obj);
//				}
//			} catch (Throwable t) {
//				helper.log("ERROR " + name,t);
//			}
//			
//		}
//		return to;
//	}

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
		if (in instanceof Integer || in instanceof Long || in instanceof Byte || in instanceof Short || in instanceof Double || in instanceof Float)
			return String.valueOf(in).replace(',', '.');
		if (in instanceof Date)
			return String.valueOf( ((Date)in).getTime() );
		return
				'"' + encode(String.valueOf(in)) + '"';
	}
	
}
