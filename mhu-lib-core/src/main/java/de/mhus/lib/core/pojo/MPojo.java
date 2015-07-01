package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Element;

import de.mhus.lib.core.MActivator;
import de.mhus.lib.core.MCast;
import de.mhus.lib.core.MString;
import de.mhus.lib.core.MXml;
import de.mhus.lib.core.logging.Log;
import de.mhus.lib.core.util.Base64;

public class MPojo {

	private static final String MAGIC = "\n\t\t\r[[";
	private static final int MAX_LEVEL = 10;
	private static Log log = Log.getLog(MPojo.class);

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
		if (level > MAX_LEVEL) return;
		PojoModel model = factory.createPojoModel(from.getClass());
		for (PojoAttribute<?> attr : model) {
			Object value = attr.get(from);
			String name = attr.getName();
			setJsonValue(to, name, value, factory, false, level+1);
		}
	}

	public static void addJsonValue(ArrayNode to, Object value, PojoModelFactory factory, boolean deep, int level) throws IOException {
		if (level > MAX_LEVEL) return;
		
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
		if (level > MAX_LEVEL) return;
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
		pojoToXml(from, to, factory, 0);
	}
	
	public static void pojoToXml(Object from, Element to, PojoModelFactory factory, int level) throws IOException {
		if (level > MAX_LEVEL) return;
		PojoModel model = factory.createPojoModel(from.getClass());
		for (PojoAttribute<?> attr : model) {
			Object value = attr.get(from);
			String name = attr.getName();
			
			Element a = to.getOwnerDocument().createElement("attribute");
			to.appendChild(a);
			a.setAttribute("name", name);
			
			if (value == null) {
				a.setAttribute("null", "true");
				//to.setAttribute(name, (String)null);
			} else
			if (value instanceof Boolean)
				a.setAttribute("boolean", MCast.toString((boolean)value));
			else
			if (value instanceof Integer)
				a.setAttribute("int", MCast.toString((int)value));
			else
			if (value instanceof Long)
				a.setAttribute("long", MCast.toString((long)value));
			else
			if (value instanceof Date)
				a.setAttribute("date", MCast.toString( ((Date)value).getTime() ));
			else
			if (value instanceof String) {
				if (hasValidChars((String)value))
					a.setAttribute("string", (String)value);
				else {
					a.setAttribute("encoding", "base64");
					a.setAttribute("string", Base64.encode( (String)value));
				}
			} else
			if (value.getClass().isEnum()) {
				a.setAttribute("enum", MCast.toString( ((Enum<?>)value).ordinal() ) );
				a.setAttribute("value", ((Enum<?>)value).name());
			}
			else
			if (value instanceof UUID) {
				a.setAttribute("uuid", ((UUID)value).toString() );
			}
			else {
				a.setAttribute("type", value.getClass().getCanonicalName());
				pojoToXml(value, a, factory, level+1);
			}
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
	
	private static boolean hasValidChars(String value) {
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c == '\n' || c == '\r' || c == '\t' || c >= 32 && c <= 55295 ) {
			} else {
				return false;
			}
		}
		return true;
	}
/*
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
*/
	public static void xmlToPojo(Element from, Object to, MActivator act) throws IOException {
		xmlToPojo(from, to, createModelFactory(), act);
	}
	
	@SuppressWarnings("unchecked")
	public static void xmlToPojo(Element from, Object to, PojoModelFactory factory, MActivator act) throws IOException {
		PojoModel model = factory.createPojoModel(to.getClass());
		
		HashMap<String, Element> index = new HashMap<>();
		for (Element e : MXml.getLocalElementIterator(from, "parameter"))
			index.put(e.getAttribute("name"), e);
		
		for (PojoAttribute<Object> attr : model) {
			String name = attr.getName();
			Class<?> type = attr.getType();
			Element a = index.get(name);
			if (a == null) {
				log.d("attribute not found",name,to.getClass());
				continue;
			}
			{
				String value = a.getAttribute("null");
				if (MString.isSet(value) && value.equals("true")) {
					attr.set(to, null);
					continue;
				}
			}
			if (a.hasAttribute("string")) {
				String data = a.getAttribute("encoding");
				if ("base64".equals(data)) {
					String value = new String( Base64.decode(a.getAttribute("string")) );
					attr.set(to, value);
				} else {
					String value = a.getAttribute("string");
					attr.set(to, value);
				}
				continue;
			}
			if (a.hasAttribute("boolean")) {
				String value = a.getAttribute("boolean");
				attr.set(to, MCast.toboolean(value, false));
				continue;
			}
			if (a.hasAttribute("int")) {
				String value = a.getAttribute("int");
				attr.set(to, MCast.toint(value,0));
				continue;
			}
			if (a.hasAttribute("long")) {
				String value = a.getAttribute("long");
				attr.set(to, MCast.tolong(value,0));
				continue;
			}
			if (a.hasAttribute("date")) {
				String value = a.getAttribute("date");
				Date obj = new Date();
				obj.setTime( MCast.tolong(value,0) );
				attr.set(to, obj);
				continue;
			}
			if (a.hasAttribute("uuid")) {
				String value = a.getAttribute("uuid");
				try {
					attr.set(to, UUID.fromString(value));
				} catch (Throwable t) {
					log.d(name,t);
				}
				continue;
			}
			if (a.hasAttribute("enum")) {
				String value = a.getAttribute("enum");
				attr.set(to, MCast.toint(value, 0));
				continue;
			}
			if (a.hasAttribute("type")) {
				String value = a.getAttribute("type");
				try {
					Object obj = act.createObject(value);
					xmlToPojo(a,obj,factory,act);
					attr.set(to, obj);
				} catch (Exception e1) {
					log.d(name,to.getClass(),e1);
				}
				continue;
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
