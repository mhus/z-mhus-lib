package de.mhus.lib.core.json;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.NotSupportedException;

public abstract class TransformStrategy extends MLog {

	public Object jsonToPojo(JsonNode node) throws NotSupportedException {
		return jsonToPojo(node,null,MJson.DEFAULT_HELPER);
	}

	public Object jsonToPojo(JsonNode node, Class<?> type) throws NotSupportedException {
		return jsonToPojo(node,type,MJson.DEFAULT_HELPER);
	}
	
	/**
	 * Transform the json into an object, some implementations need a type hint to create the object.
	 * 
	 * @param node The object data
	 * @param type The type hint, can also be null. If this is not supportet throw the exception
	 * @param helper The helper to create objects
	 * @return The object, can also be null if the object originally was null.
	 * @throws NotSupportedException Thrown if the object could not be created.
	 */
	public abstract Object jsonToPojo(JsonNode node, Class<?> type, TransformHelper helper) throws NotSupportedException;
	

	public JsonNode pojoToJson(Object obj) throws NotSupportedException {
		return pojoToJson(obj, MJson.DEFAULT_HELPER);
	}

	/**
	 * Transform an object into an json representation. 
	 * @param obj The object to transform
	 * @param helper 
	 * @return The json representation, never return null. Throw the exception if problems occur.
	 * @throws NotSupportedException
	 */
	public abstract JsonNode pojoToJson(Object obj, TransformHelper helper) throws NotSupportedException;
	
	
}
