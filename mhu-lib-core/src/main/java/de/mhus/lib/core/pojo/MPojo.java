package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Element;

import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.util.Base64;

public class MPojo {

	private static final String MAGIC = "\n\t\t\r[[";

	private static PojoModelFactory createModelFactory() {
		return new PojoModelFactory() {
			
			@Override
			public PojoModel createPojoModel(Class<?> pojoClass) {
				PojoModel model = new PojoParser().parse(pojoClass,"_",null).filter(new DefaultFilter(true, false, false, false, true) ).getModel();
				return model;
			}
		};
	}
	
	public static void pojoToJson(Object from, ObjectNode to) throws IOException {
		pojoToJson(from, to, createModelFactory());
	}
	
	public static void pojoToJson(Object from, ObjectNode to, PojoModelFactory factory) throws IOException {
		pojoToJson(from, to, factory, 0);
	}
	
	public static void pojoToJson(Object from, ObjectNode to, PojoModelFactory factory, int level) throws IOException {
		if (level > 10) return;
		PojoModel model = factory.createPojoModel(from.getClass());
		for (PojoAttribute<?> attr : model) {
			Object value = attr.get(from);
			String name = attr.getName();
			setJsonValue(to, name, value, factory, false, level+1);
		}
	}

	public static void addJsonValue(ArrayNode to, Object value, PojoModelFactory factory, boolean deep, int level) throws IOException {
		if (level > 10) return;
		
		if (value == null)
			to.addNull();
		else
		if (value instanceof Boolean)
			to.add((boolean)value);
		else
		if (value instanceof Integer)
			to.add((int)value);
		else
		if (value instanceof String)
			to.add((String)value);
		else
		if (value instanceof Long)
			to.add((Long)value);
		else
		if (value instanceof byte[])
			to.add((byte[])value);
		else
		if (value instanceof Float)
			to.add((Float)value);
		else
		if (value instanceof BigDecimal)
			to.add((BigDecimal)value);
		else
		if (value instanceof JsonNode)
			to.add((JsonNode)value);
		else
		if (value.getClass().isEnum()) {
			to.add(((Enum<?>)value).ordinal());
//			to.put(name + "_", ((Enum<?>)value).name());
		} else
		if (value instanceof Map) {
			ObjectNode obj = to.objectNode();
			to.add(obj);
			for (Map.Entry<Object, Object> entry : ((Map<Object,Object>)value).entrySet()) {
				setJsonValue(obj, String.valueOf(entry.getKey()), entry.getValue(), factory, true, level+1 );
			}
		} else
		if (value instanceof Collection) {
			ArrayNode array = to.arrayNode();
			to.add(array);
			for (Object o : ((Collection<Object>)value)) {
				addJsonValue(array,o,factory,true,level+1);
			}
		} else {
			if (deep) {
				ObjectNode too = to.objectNode();
				to.add(too);
				pojoToJson(value, too, null, level+1);
			} else {
				to.add(String.valueOf(value));
			}
		}		
	}
	
	public static void setJsonValue(ObjectNode to, String name, Object value, PojoModelFactory factory, boolean deep, int level) throws IOException {
		if (level > 10) return;
		if (value == null)
			to.putNull(name);
		else
		if (value instanceof Boolean)
			to.put(name, (boolean)value);
		else
		if (value instanceof Integer)
			to.put(name, (int)value);
		else
		if (value instanceof String)
			to.put(name, (String)value);
		else
		if (value instanceof Long)
			to.put(name, (Long)value);
		else
		if (value instanceof byte[])
			to.put(name, (byte[])value);
		else
		if (value instanceof Float)
			to.put(name, (Float)value);
		else
		if (value instanceof BigDecimal)
			to.put(name, (BigDecimal)value);
		else
		if (value instanceof JsonNode)
			to.put(name, (JsonNode)value);
		else
		if (value.getClass().isEnum()) {
			to.put(name, ((Enum<?>)value).ordinal());
			to.put(name + "_", ((Enum<?>)value).name());
		} else
		if (value instanceof Map) {
			ObjectNode obj = to.objectNode();
			to.put(name, obj);
			for (Map.Entry<Object, Object> entry : ((Map<Object,Object>)value).entrySet()) {
				setJsonValue(obj, String.valueOf(entry.getKey()), entry.getValue(), factory, true, level+1 );
			}
		} else
		if (value instanceof Collection) {
			ArrayNode array = to.arrayNode();
			to.put(name, array);
			for (Object o : ((Collection<Object>)value)) {
				addJsonValue(array,o,factory,true,level+1);
			}
		} else {
			if (deep) {
				ObjectNode too = to.objectNode();
				to.put(name, too);
				pojoToJson(value, too, factory, level+1);
			} else {
				to.put(name, String.valueOf(value));
			}
		}
	}

	public static void jsonToPojo(JsonNode from, Object to) throws IOException {
		jsonToPojo(from, to, createModelFactory());
	}
	
	@SuppressWarnings("unchecked")
	public static void jsonToPojo(JsonNode from, Object to, PojoModelFactory factory) throws IOException {
		PojoModel model = factory.createPojoModel(to.getClass());
		for (PojoAttribute<Object> attr : model) {
			String name = attr.getName();
			Class<?> type = attr.getType();
			JsonNode json = from.get(name);
			
			try {
				if (json == null || !attr.canWrite() ) {
					
				} else
				if (type == Boolean.class || type == boolean.class)
					attr.set(to, json.getValueAsBoolean(false));
				else
				if (type == Integer.class || type == int.class)
					attr.set(to, json.getValueAsInt(0));
				else
				if (type == String.class)
					attr.set(to, json.getValueAsText());
				else
				if (type == UUID.class)
					try {
						attr.set(to, UUID.fromString(json.getValueAsText()));
					} catch (IllegalArgumentException e) {
						attr.set(to, null);
					}
				else
				if (type.isEnum()) {
					Object[] cons=type.getEnumConstants();
					int ord = json.getValueAsInt(0);
					Object c = cons.length > 0 ? cons[0] : null;
					if (ord >=0 && ord < cons.length) c = cons[ord];
					attr.set(to, c );
				}
				else
					attr.set(to, json.getValueAsText());
			} catch (Throwable t) {
				System.out.println("ERROR " + name);
				t.printStackTrace();
			}
		}
	}

	public static void pojoToXml(Object from, Element to) throws IOException {
		pojoToXml(from, to, createModelFactory());
	}

	public static void pojoToXml(Object from, Element to, PojoModelFactory factory) throws IOException {
		PojoModel model = factory.createPojoModel(from.getClass());
		for (PojoAttribute<?> attr : model) {
			Object value = attr.get(from);
			String name = attr.getName();
			if (value == null) {
				//to.setAttribute(name, (String)null);
			} else
			if (value instanceof Boolean)
				to.setAttribute(name, MCast.toString((boolean)value));
			else
			if (value instanceof Integer)
				to.setAttribute(name, MCast.toString((int)value));
			else
			if (value instanceof String)
				to.setAttribute(name, toAttribute( (String)value) );
			else
			if (value.getClass().isEnum()) {
				to.setAttribute(name, MCast.toString( ((Enum<?>)value).ordinal() ) );
				//to.setAttribute(name + "_", ((Enum<?>)value).name());
			}
			else
				to.setAttribute(attr.getName(), toAttribute( MCast.toString(value)) );
		}
	}

	private static String fromAttribute(String value) {
		if (value == null) return null;
		if (value.equals(MAGIC + "null")) return null;
		if (value.startsWith(MAGIC)) {
			new String( Base64.decode( value.substring(MAGIC.length()) ) );
		}
		return value;
	}
	private static String toAttribute(String value) {
		if (value == null) return MAGIC + "null";
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c == '\n' || c == '\r' || c == '\t' || c >= 32 && c <= 55295 ) {
			} else {
				return MAGIC + Base64.encode(value);
			}
		}
		return value;
	}

	public static void xmlToPojo(Element from, Object to) throws IOException {
		xmlToPojo(from, to, createModelFactory());
	}
	
	@SuppressWarnings("unchecked")
	public static void xmlToPojo(Element from, Object to, PojoModelFactory factory) throws IOException {
		PojoModel model = factory.createPojoModel(to.getClass());
		for (PojoAttribute<Object> attr : model) {
			String name = attr.getName();
			Class<?> type = attr.getType();
			String value = from.getAttribute(name);
			
			try {
				if (value == null || !attr.canWrite() ) {
					
				} else
				if (type == Boolean.class || type == boolean.class)
					attr.set(to, value);
				else
				if (type == Integer.class || type == int.class)
					attr.set(to, value);
				else
				if (type == String.class)
					attr.set(to, fromAttribute( value ) );
				else
				if (type == UUID.class)
					try {
						attr.set(to, UUID.fromString(value));
					} catch (IllegalArgumentException e) {
						attr.set(to, null);
					}
				else
				if (type.isEnum()) {
					Object[] cons=type.getEnumConstants();
					int ord = MCast.toint(value, 0);
					Object c = cons.length > 0 ? cons[0] : null;
					if (ord >=0 && ord < cons.length) c = cons[ord];
					attr.set(to, c );
				}
				else
					attr.set(to, fromAttribute( value ) );
			} catch (Throwable t) {
				System.out.println("ERROR " + name);
				t.printStackTrace();
			}
		}
	}
	
	/**
	 * Functionize a String. Remove bad names and set first characters to upper. Return def if the name
	 * can't be created, e.g. only numbers.
	 * 
	 * @param in
	 * @param firstUpper 
	 * @param def
	 * @return
	 */
	public static String toFunctionName(String in, boolean firstUpper,String def) {
		if (MString.isEmpty(in)) return def;
		boolean first = firstUpper;
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			if (c >= 'a' && c <= 'z' || c >='A' && c <='Z' || c == '_') {
				if (first)
					c = Character.toUpperCase(c);
				first = false;
				out.append(c);
			} else
			if (!first && c >='0' && c <= '9') {
				out.append(c);
			} else {
				first = true;
			}
		}
		
		if (out.length() == 0) return def;
		return out.toString();
	}
	
	
	
}
