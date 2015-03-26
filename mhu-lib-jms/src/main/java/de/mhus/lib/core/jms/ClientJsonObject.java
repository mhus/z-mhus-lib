package de.mhus.lib.core.jms;

import java.io.IOException;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MJson.TransformHelper;

public class ClientJsonObject extends ClientJson {

	private TransformHelper helper;

	public ClientJsonObject(JmsDestination dest) {
		super(dest);
	}
	
	public void sendObjectOneWay(IProperties prop, Object ... obj) throws JMSException, IOException {
		ObjectNode json = MJson.createObjectNode();
		MJson.pojoToJson(obj, json, helper);
		sendJsonOneWay(prop, json);
	}

	public RequestResult<Object> sendObject(IProperties prop, Object ... obj) throws JMSException, IOException {
		ObjectNode json = MJson.createObjectNode();
		MJson.pojoToJson(obj, json);
		RequestResult<JsonNode> res = sendJson(prop, json);
		if (res == null) return null;
		
		Object o = MJson.jsonToPojo(res.getResult(), null, helper);
		
		return new RequestResult<Object>(o, prop);
	}

	public TransformHelper getHelper() {
		return helper;
	}

	public void setHelper(TransformHelper helper) {
		this.helper = helper;
	}

}