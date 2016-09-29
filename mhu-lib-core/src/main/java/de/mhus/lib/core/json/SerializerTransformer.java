package de.mhus.lib.core.json;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.BooleanNode;
import org.codehaus.jackson.node.DoubleNode;
import org.codehaus.jackson.node.IntNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.pojo.PojoAttribute;
import de.mhus.lib.core.pojo.PojoModel;
import de.mhus.lib.errors.NotSupportedException;

/**
 * <p>SerializerTransformer class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public class SerializerTransformer extends TransformStrategy {

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public Object jsonToPojo(JsonNode from, Class<?> typex,
			TransformHelper helper) throws NotSupportedException {

		if (from instanceof TextNode) return ((TextNode)from).getTextValue();
		if (from instanceof ArrayNode) {
			ArrayNode node = (ArrayNode)from;
			LinkedList<Object> list = new LinkedList<>();
			JsonNode first = null;
			for (JsonNode item : node) {
				if (first == null)
					first = item;
				else {
					list.add(jsonToPojo(item, null, helper));
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

		if (MJson.getValue(from, "_null", false)) return null; // return null is ok !
		
		if (from instanceof IntNode) return ((IntNode)from).getIntValue();
		if (from instanceof BooleanNode) return ((BooleanNode)from).getBooleanValue();
		if (from instanceof DoubleNode) return ((DoubleNode)from).getDoubleValue();
		
		if (!(from instanceof ObjectNode)) throw new NotSupportedException("node type is unknown");
		
		String clazz = MJson.getValue(from,"_type", "");
		String special = MJson.getValue(from,"_special", "");
		if ("json".equals(special)) {
			((ObjectNode)from).remove("_special");
			return from;
		}
		
		JsonNode val = MJson.getByPath(from, "_value");
		
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
			
			} catch (IOException e) {
				log().i(e);
				throw new NotSupportedException("exception",e);
			}
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
					out.put(field.getKey(), jsonToPojo(field.getValue(), null, helper) );
				}
			}
			return out;
		} else
		if (special.equals("collection")) {
			ArrayNode array = (ArrayNode) from.get("_array");
			Object[] a = (Object[]) jsonToPojo(array, null, helper);
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
					Object value = jsonToPojo(from.get(name), null, helper);
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
				log().i(t);
				throw new NotSupportedException("exception", t);
			}
		}
				
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public JsonNode pojoToJson(Object from, TransformHelper helper)
			throws NotSupportedException {

		if (from == null) {
			ObjectNode first = MJson.createObjectNode();
			first.put("_null", true);
			return first;
		}
		
		if (from instanceof ObjectNode) {
			((ObjectNode)from).put("_special", "json");
			return (ObjectNode)from;
		}
		
		if (from.getClass().isArray()) {
			ArrayNode out = MJson.createArrayNode();

			ObjectNode first = MJson.createObjectNode();
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
			ObjectNode out = MJson.createObjectNode();
			
			if (from instanceof String || from instanceof Integer || from instanceof Boolean || from instanceof Short || from instanceof Long || from instanceof Double || from instanceof Float || from instanceof Character || from instanceof Byte) {
				out.put("_type", from.getClass().getCanonicalName());
				putPojoValue(out, "_value", from, helper);
			} else
			if (from instanceof Map) {
				out.put("_type", from.getClass().getCanonicalName());
				out.put("_special", "map");
				@SuppressWarnings({ "rawtypes" })
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
				@SuppressWarnings({ "rawtypes"})
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

	/**
	 * <p>putPojoValue.</p>
	 *
	 * @param out a {@link org.codehaus.jackson.node.ObjectNode} object.
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @param helper a {@link de.mhus.lib.core.json.TransformHelper} object.
	 */
	protected void putPojoValue(ObjectNode out, String name, Object value, TransformHelper helper) {
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

}
