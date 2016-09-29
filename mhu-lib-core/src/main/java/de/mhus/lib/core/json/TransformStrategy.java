package de.mhus.lib.core.json;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MLog;
import de.mhus.lib.errors.NotSupportedException;

/**
 * <p>Abstract TransformStrategy class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 */
public abstract class TransformStrategy extends MLog {

	/**
	 * <p>jsonToPojo.</p>
	 *
	 * @param node a {@link org.codehaus.jackson.JsonNode} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws de.mhus.lib.errors.NotSupportedException if any.
	 */
	public Object jsonToPojo(JsonNode node) throws NotSupportedException {
		return jsonToPojo(node,null,MJson.DEFAULT_HELPER);
	}

	/**
	 * <p>jsonToPojo.</p>
	 *
	 * @param node a {@link org.codehaus.jackson.JsonNode} object.
	 * @param type a {@link java.lang.Class} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws de.mhus.lib.errors.NotSupportedException if any.
	 */
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
	 * @throws de.mhus.lib.errors.NotSupportedException Thrown if the object could not be created.
	 */
	public abstract Object jsonToPojo(JsonNode node, Class<?> type, TransformHelper helper) throws NotSupportedException;
	

	/**
	 * <p>pojoToJson.</p>
	 *
	 * @param obj a {@link java.lang.Object} object.
	 * @return a {@link org.codehaus.jackson.JsonNode} object.
	 * @throws de.mhus.lib.errors.NotSupportedException if any.
	 */
	public JsonNode pojoToJson(Object obj) throws NotSupportedException {
		return pojoToJson(obj, MJson.DEFAULT_HELPER);
	}

	/**
	 * Transform an object into an json representation.
	 *
	 * @param obj The object to transform
	 * @param helper a {@link de.mhus.lib.core.json.TransformHelper} object.
	 * @return The json representation, never return null. Throw the exception if problems occur.
	 * @throws de.mhus.lib.errors.NotSupportedException if any.
	 */
	public abstract JsonNode pojoToJson(Object obj, TransformHelper helper) throws NotSupportedException;
	
	
}
