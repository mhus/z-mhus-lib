package de.mhus.lib.core.json;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.MJson;
import de.mhus.lib.errors.NotSupportedException;

/**
 * <p>JacksonTransformer class.</p>
 *
 * @author mikehummel
 * @version $Id: $Id
 * @since 3.2.9
 */
public class JacksonTransformer extends TransformStrategy {

	/** {@inheritDoc} */
	@Override
	public Object jsonToPojo(JsonNode node, Class<?> type,
			TransformHelper helper) throws NotSupportedException {
		
		try {
			return MJson.getMapper().readValue(node, type);
		} catch (Exception e) {
			throw new NotSupportedException(e);
		}

	}

	/** {@inheritDoc} */
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
