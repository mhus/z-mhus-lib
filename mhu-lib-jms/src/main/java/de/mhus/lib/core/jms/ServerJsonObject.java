package de.mhus.lib.core.jms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.node.ObjectNode;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MJson.TransformHelper;

public abstract class ServerJsonObject extends ServerJson {

	private TransformHelper helper = new TransformHelper();

	public ServerJsonObject(JmsDestination dest) {
		super(dest);
	}

	public abstract void receivedOneWay(IProperties properties, Object ... obj);

	public abstract RequestResult<Object> received(IProperties properties, Object ... obj);

	@Override
	public void receivedOneWay(IProperties properties, JsonNode node) {
		try {
			JsonNode array = node.get("array");
			LinkedList<Object> out = new LinkedList<>();
			for (JsonNode item : array) {
				Object to = MJson.jsonToPojo(item, null, helper); //TODO security of creating every object!!!
				out.add(to);
			}
			receivedOneWay(properties, out.toArray(new Object[out.size()]));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public RequestResult<JsonNode> received(IProperties properties, JsonNode node) {
		try {
			JsonNode array = node.get("array");
			LinkedList<Object> out = new LinkedList<>();
			for (JsonNode item : array) {
				Object to = MJson.jsonToPojo(item, null, helper); //TODO security of creating every object!!!
				out.add(to);
			}
			RequestResult<Object> res = received(properties, out.toArray(new Object[out.size()]));
			if (res == null) return null;
			
			ObjectNode to = MJson.createObjectNode();
			MJson.pojoToJson(res.getResult(), to, helper);
			
			return new RequestResult<JsonNode>(to, res.getProperties());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

}
