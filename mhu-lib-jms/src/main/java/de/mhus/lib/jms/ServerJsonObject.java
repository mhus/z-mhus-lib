package de.mhus.lib.jms;

import java.util.LinkedList;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import de.mhus.lib.core.AbstractProperties;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.json.SecurityTransformHelper;

public abstract class ServerJsonObject extends ServerJson {

	private SecurityTransformHelper helper = new SecurityTransformHelper(null, log());

	public ServerJsonObject(JmsDestination dest) {
		super(dest);
	}

	public abstract void receivedOneWay(AbstractProperties properties, Object ... obj);

	public abstract RequestResult<Object> received(AbstractProperties properties, Object ... obj);

	@Override
	public void receivedOneWay(AbstractProperties properties, JsonNode node) {
		try {
			JsonNode array = node.get("array");
			LinkedList<Object> out = new LinkedList<>();
			if (array != null) {
				for (JsonNode item : array) {
					Object to = MJson.jsonToPojo((ObjectNode) item, helper);
					out.add(to);
				}
			}
			receivedOneWay(properties, out.toArray(new Object[out.size()]));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public RequestResult<JsonNode> received(AbstractProperties properties, JsonNode node) {
		try {
//			JsonNode array = node.get("array");
//			LinkedList<Object> out = new LinkedList<>();
//			if (array != null) {
//				for (JsonNode item : array) {
//					Object to = MJson.jsonToPojo((ObjectNode) item, helper);
//					out.add(to);
//				}
//			}
//			RequestResult<Object> res = received(properties, out.toArray(new Object[out.size()]));
			Object[] attr = (Object[]) MJson.jsonToPojo(node, helper);
			
			RequestResult<Object> res = received(properties, attr);
			if (res == null) return null;
			
			JsonNode to = MJson.pojoToJson(res.getResult(), helper);
			
			return new RequestResult<JsonNode>(to, res.getProperties());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	public SecurityTransformHelper getHelper() {
		return helper;
	}

	public void setHelper(SecurityTransformHelper helper) {
		this.helper = helper;
	}

}
