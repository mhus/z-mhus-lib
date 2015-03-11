package de.mhus.lib.core;

import java.util.Date;

import org.codehaus.jackson.JsonNode;

public class MJson {

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
	
	@SuppressWarnings("unchecked")
	public static <T> T getValue(JsonNode parent, String path, T def ) {
		JsonNode node = getByPath(parent, path);
		if (node == null) return def;
		Object out = null;
		
		if (def == null || def instanceof String)
			out = node.getValueAsText();
		else
		if (def instanceof Boolean)
			out = node.getValueAsBoolean((Boolean) def);
		else
		if (def instanceof Integer)
			out = node.getValueAsInt((Integer) def);
		else
		if (def instanceof Date)
			out = new Date(node.getValueAsLong(((Date)def).getTime()));
		else
		if (def instanceof Long)
			out = node.getValueAsLong((Long) def);
		else
		if (def instanceof Double)
			out = node.getValueAsDouble((Double) def);

		if (out == null) 
			out = def;
		return (T) out;
	}
	
	public static String getText(JsonNode parent, String path, String def ) {
		JsonNode node = getByPath(parent, path);
		if (node == null) return def;
		String out = null;
		out = node.getTextValue();
		if (out == null) out = def;
		return out;
	}
	
}
