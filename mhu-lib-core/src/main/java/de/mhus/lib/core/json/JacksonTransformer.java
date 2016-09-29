package de.mhus.lib.core.json;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.errors.NotSupportedException;

public class JacksonTransformer extends TransformStrategy {

	@Override
	public Object jsonToPojo(JsonNode node, Class<?> type,
			TransformHelper helper) throws NotSupportedException {
		
		try {
			return MJson.getMapper().readValue(node, type);
		} catch (Exception e) {
			throw new NotSupportedException(e);
		}

	}

	@Override
	public JsonNode pojoToJson(Object obj, TransformHelper helper)
			throws NotSupportedException {
		JsonNode x;
		try {
			x = MJson.load( MJson.getMapper().writeValueAsString(obj) );
		} catch (Exception e) {
			throw new NotSupportedException(e);
		}
		return x;
	}

}
