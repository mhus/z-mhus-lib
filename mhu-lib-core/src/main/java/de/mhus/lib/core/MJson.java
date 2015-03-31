package de.mhus.lib.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.BooleanNode;
import org.codehaus.jackson.node.DoubleNode;
import org.codehaus.jackson.node.IntNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;
import org.codehaus.jackson.type.TypeReference;

import de.mhus.lib.core.json.TransformHelper;
import de.mhus.lib.core.pojo.DefaultFilter;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.core.pojo.PojoParser;

public class MJson {
	
	private static final TransformHelper DEFAULT_HELPER = new TransformHelper();
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
		return jsonToPojo(from, null);
	}

	public static ObjectNode createObjectNode() {
		return mapper.createObjectNode();
	}
	
//	@SuppressWarnings("unchecked")
//	public static void pojoToJson(Object from, ObjectNode to, TransformHelper helper) throws IOException {
//		if (from == null) return; //TODO maybe add a null information in the pojo
//		if (helper == null) helper = DEFAULT_HELPER;
//		
//		if (from instanceof Object[]) {
//			// it's an array
//			ArrayNode array = to.arrayNode();
//			to.put("array", array);
//			for (Object i : (Object[])from) {
//				ObjectNode o = array.addObject();
//				pojoToJson(i, o, helper.incLevel());
//			}
//			helper.decLevel();
//			return;
//		}
//		
//		if (from instanceof Collection) {
//			
//			Collection<?> obj = (Collection<?>)from;
//			ArrayNode array = to.arrayNode();
//			to.put("_collection", array);
//			for (Object o : obj) {
//				ObjectNode item = array.objectNode();
//				pojoToJson(o, item, helper.incLevel());
//				array.add(item);
//			}
//			helper.decLevel();
//			return;
//		}
//
//		if (from instanceof Map) {
//			ObjectNode obj = to.objectNode();
//			to.put("_map", obj);
//			for (Map.Entry<String,Object> e : ((Map<String,Object>)from).entrySet() ) {
//				ObjectNode on = obj.objectNode();
//				obj.put(String.valueOf(e.getKey()), on);
//				pojoToJson(e.getValue(), on);
//			}
//			helper.decLevel();
//			return;
//		}
//		
//		PojoModel model = helper.createPojoModel(from);
//		for (PojoAttribute<?> attr : model) {
//			try {
//				Object value = attr.get(from);
//				String name = attr.getName();
//				if (value == null)
//					to.put(name, (String)null);
//				else
//				if (value instanceof Boolean)
//					to.put(name, (boolean)value);
//				else
//				if (value instanceof Integer)
//					to.put(name, (int)value);
//				else
//				if (value instanceof String)
//					to.put(name, (String)value);
//				else
//				if (value.getClass().isEnum()) {
//					to.put(name, ((Enum<?>)value).ordinal());
//					to.put(name + "_", ((Enum<?>)value).name());
//				}
//				else
//				if (value instanceof Date)
//					to.put(name, ((Date)value).getTime() );
//				else
//				if (value instanceof String[]) {
//					ArrayNode array = to.arrayNode();
//					to.put(name, array);
//					for (String i : (String[])value)
//						array.add(i);
//				} else
//				if (value instanceof Object[]) {
//					ArrayNode array = to.arrayNode();
//					to.put(name, array);
//					for (Object i : (Object[])value) {
//						ObjectNode o = array.addObject();
//						pojoToJson(i, o, helper.incLevel());
//					}
//				} else
//				if (value instanceof Map) {
//					ObjectNode obj = to.objectNode();
//					to.put(name, obj);
//					for (Map.Entry<String,Object> e : ((Map<String,Object>)value).entrySet() )
//						obj.put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
//				} else
//				if (value instanceof Collection) {
//					Collection<?> obj = (Collection<?>)value;
//					ArrayNode array = to.arrayNode();
//					to.put(name, array);
//					for (Object o : obj) {
//						ObjectNode item = array.objectNode();
//						pojoToJson(o, item, helper.incLevel());
//						array.add(item);
//					}
//					
//				} else
//				if (value instanceof Class) {
//					to.put(attr.getName(), ((Class<?>)value).getSimpleName());
//				} else
//				if (value instanceof UUID) {
//					to.put(attr.getName(), String.valueOf(value));
//				} else {
//					if (!helper.checkLevel()) {
//						helper.log("too deep:" + attr.getName() + " " + value.getClass().getSimpleName());
//						return;
//					}
//					ObjectNode sub = to.objectNode();
//					pojoToJson(value, sub, helper.incLevel());
//					to.put(attr.getName(), sub);
//				}
//			} catch (Throwable t) {
//				helper.log(null,t);
//			}
//		}
//		helper.decLevel();
//		helper.postToJson(from, to);
//	}

	
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

// try to use build in
	
//	protected static JsonNode pojoToJson(Object from, TransformHelper helper) throws IOException {
//		JsonNode x = load( mapper.writeValueAsString(from) );
//		return x;
//	}
//
//	public static Object jsonToPojo(JsonNode from, TransformHelper helper) throws IOException, IllegalAccessException {
//		if (helper == null) helper = DEFAULT_HELPER;
//		JsonNode x = from.get("_object");
//		if (x == null || x instanceof TextNode && "null".equals(((TextNode)x).getTextValue()) ) return null;
//		Class<?> type = helper.getType(from);
//		boolean isArray = helper.isArrayType(from);
//		if (isArray) {
//			Object[] array = helper.createArray(x,type);
//			int cnt = 0;
//			for (JsonNode item : x) {
//				array[cnt] = mapper.readValue(item, type);
//				cnt++;
//			}
//			return array;
//		} else
//			return mapper.readValue(x, type);
//	}

	public static JsonNode pojoToJson(Object from, TransformHelper helper) {
		if (helper == null) helper = DEFAULT_HELPER;
		if (from == null) {
			ObjectNode first = mapper.createObjectNode();
			first.put("_null", true);
			return first;
		}
		
		if (from instanceof ObjectNode) {
			((ObjectNode)from).put("_special", "json");
			return (ObjectNode)from;
		}
		
		if (from.getClass().isArray()) {
			ArrayNode out = mapper.createArrayNode();

			ObjectNode first = mapper.createObjectNode();
			first.put("_arraytype", from.getClass().getCanonicalName() );
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
			ObjectNode out = mapper.createObjectNode();
			
			if (from instanceof String || from instanceof Integer || from instanceof Boolean || from instanceof Short || from instanceof Long || from instanceof Double || from instanceof Float || from instanceof Character || from instanceof Byte) {
				out.put("_type", from.getClass().getCanonicalName());
				putPojoValue(out, "_value", from, helper);
			} else
			if (from instanceof Map) {
				out.put("_type", from.getClass().getCanonicalName());
				out.put("_special", "map");
				Map<Object,Object> map = (Map)from;
				ObjectNode x = out.objectNode();
				out.put("_map", x);
				for (Map.Entry<Object,Object> en : map.entrySet()) {
					putPojoValue(x, String.valueOf(en.getKey()), en.getValue(), helper);
				}
					
			} else
			if (from instanceof Collection) {
				out.put("_type", from.getClass().getCanonicalName());
				out.put("_special", "collection");
				Collection<Object> col = (Collection)from;
				out.put("_array", pojoToJson( col.toArray() ) ); 
			} else {
				out.put("_type", from.getClass().getCanonicalName());
				
				PojoModel model = helper.createPojoModel(from);
				for (PojoAttribute<Object> attr : model) {
					String name = attr.getName();
					try {
						putPojoValue( out, name, attr.get(from), helper);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			helper.decLevel();
			return out;
		}
	}

	private static void putPojoValue(ObjectNode out, String name, Object value, TransformHelper helper) {
		if (value == null) 
			out.putNull(name);
		else
		if (value instanceof Byte)
			out.put(name, new byte[] {(Byte)value});
		else
		if (value instanceof String)
			out.put(name, (String)value);
		else
		if (value instanceof Long)
			out.put(name, (Long)value);
		else
		if (value instanceof Integer)
			out.put(name, (Integer)value);
		else
		if (value instanceof Double)
			out.put(name, (Double)value);
		else
		if (value instanceof Short)
			out.put(name, (Short)value);
		else
		if (value instanceof Float)
			out.put(name, (Double)value);
		else
		if (value instanceof Character)
			out.put(name, (Character)value);
		else
		if (value instanceof Boolean)
			out.put(name, (Boolean)value);
		else
			out.put(name, pojoToJson(value, helper.incLevel()) );
	}

	public static Object jsonToPojo(JsonNode from, TransformHelper helper) {
		if (helper == null) helper = DEFAULT_HELPER;

		if (from instanceof TextNode) return ((TextNode)from).getTextValue();
		if (from instanceof ArrayNode) {
			ArrayNode node = (ArrayNode)from;
			LinkedList<Object> list = new LinkedList<>();
			JsonNode first = null;
			for (JsonNode item : node) {
				if (first == null)
					first = item;
				else {
					list.add(jsonToPojo(item, helper));
				}
			}
			String aclazz = first.get("_arraytype").getTextValue();
			Object array = null;
			try {
				array = helper.createArray(list.size(), helper.getType(aclazz));
				for (int i = 0; i < list.size(); i++)
					Array.set(array, i, list.get(i));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return array;
		}

		if (getValue(from, "_null", false)) return null;
		
		if (from instanceof IntNode) return ((IntNode)from).getIntValue();
		if (from instanceof BooleanNode) return ((BooleanNode)from).getBooleanValue();
		if (from instanceof DoubleNode) return ((DoubleNode)from).getDoubleValue();
		
		if (!(from instanceof ObjectNode)) return null;
		
		String clazz = getValue(from,"_type", "");
		String special = getValue(from,"_special", "");
		if ("json".equals(special)) {
			((ObjectNode)from).remove("_special");
			return from;
		}
		
		JsonNode val = getByPath(from, "_value");
		
		if (val != null) {
			try {
				switch (clazz) {
				case "java.lang.String": return val.getTextValue();
				case "java.lang.Boolean": return val.getBooleanValue();
				case "java.lang.Byte": return val.getBinaryValue()[0];
				case "java.lang.Integer": return val.getIntValue();
				case "java.lang.Short": return val.getBigIntegerValue().shortValue();
				case "java.lang.Double": return val.getDoubleValue();
				case "java.lang.Float": return val.getBigIntegerValue().floatValue();
				case "java.lang.Long": return val.getBigIntegerValue().longValue();
				case "java.lang.Character": return (char)val.getIntValue();
				}
			
			} catch (IOException e) {return null;}
//			Object direct = getValue(val, helper);
//			if (direct != null) {
//				if (direct instanceof byte[])
//					return ((byte[])direct)[0];
//				return direct;
//			}
		}

		

		if (special.equals("map")) {
			ObjectNode map = (ObjectNode) from.get("_map");
			Map<Object,Object> out = null;
			try {
				out = (Map<Object, Object>) helper.createObject(helper.getType(clazz));
			} catch (Throwable e) {
				out = new HashMap<>();
			}
			Iterator<Entry<String, JsonNode>> fieldIter = map.getFields();
			while (fieldIter.hasNext()) {
				Entry<String, JsonNode> field = fieldIter.next();
				if (!field.getKey().startsWith("_")) {
					out.put(field.getKey(), jsonToPojo(field.getValue(), helper) );
				}
			}
			return out;
		} else
		if (special.equals("collection")) {
			ArrayNode array = (ArrayNode) from.get("_array");
			Object[] a = (Object[]) jsonToPojo(array, helper);
			Collection<Object> out = null;
			try {
				out = (Collection<Object>) helper.createObject(helper.getType(clazz));
			} catch (Throwable e) {
				out = new LinkedList<>();
			}
			for (Object o : a)
				out.add(o);
			return out;
		} else {
			try {
				Object to = helper.createObject(helper.getType(clazz));
				PojoModel model = helper.createPojoModel(to);
				for (PojoAttribute<Object> attr : model) {
					String name = attr.getName();
					Class<?> type = attr.getType();
					Object value = jsonToPojo(from.get(name), helper);
					if (value == null)
						attr.set(to, null);
					else
					if (type.isInstance(value))
						attr.set(to, value);
					else
						System.out.println("Can't set ...");
				}
				return to;
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
		}
		
		
		
	}
	
}
