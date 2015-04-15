package de.mhus.lib.core.pojo;

import java.io.IOException;
import java.util.UUID;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

public class MPojo {

	public static void pojoToJson(Object from, ObjectNode to) throws IOException {
		PojoModel model = new PojoParser().parse(from,"_",null).filter(new DefaultFilter(true, false, true, false, true) ).getModel();
		for (PojoAttribute<?> attr : model) {
			Object value = attr.get(from);
			String name = attr.getName();
			if (value == null)
				to.put(name, (String)null);
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
			if (value.getClass().isEnum()) {
				to.put(name, ((Enum<?>)value).ordinal());
				to.put(name + "_", ((Enum<?>)value).name());
			}
			else
				to.put(attr.getName(), String.valueOf(value));
		}
	}

	@SuppressWarnings("unchecked")
	public static void jsonToPojo(JsonNode from, Object to) throws IOException {
		PojoModel model = new PojoParser().parse(to,"_",null).filter(new DefaultFilter(true, false, false, false, true) ).getModel();
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

}
