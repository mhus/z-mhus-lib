package de.mhus.lib.jms;

import java.io.IOException;
import java.util.LinkedList;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.json.SecurityTransformHelper;

public class ClientJsonObject extends ClientJson {

	private SecurityTransformHelper helper = new SecurityTransformHelper(null, this.log());

	public ClientJsonObject(JmsDestination dest) {
		super(dest);
	}
	
	public void sendObjectOneWay(IProperties prop, Object ... obj) throws JMSException, IOException {
		JsonNode json = MJson.pojoToJson(obj, helper);
		sendJsonOneWay(prop, json);
	}

	public RequestResult<Object> sendObject(IProperties prop, Object ... obj) throws JMSException, IOException, IllegalAccessException {
		JsonNode json = MJson.pojoToJson(obj, helper);
		RequestResult<JsonNode> res = sendJson(prop, json);
		if (res == null) return null;
		
		Object o = MJson.jsonToPojo(res.getResult(), helper);
		
		return new RequestResult<Object>(o, res.getProperties());
	}

	@SuppressWarnings("unchecked")
	public RequestResult<Object>[] sendObjectBroadcast(IProperties prop, Object ... obj) throws JMSException, IOException, IllegalAccessException {
		//ObjectNode json = MJson.createObjectNode();
		JsonNode json = MJson.pojoToJson(obj, helper);
		RequestResult<JsonNode>[] answers = sendJsonBroadcast(prop, json);
		
		LinkedList<RequestResult<Object>> out = new LinkedList<>();
		
		for (RequestResult<JsonNode> res : answers) {
			if (res == null) continue;
		
			Object o = MJson.jsonToPojo(res.getResult(), helper);
		
			out.add(new RequestResult<Object>(o, res.getProperties()) );
		}
		
		return out.toArray(new RequestResult[out.size()]);
	}
	
	public SecurityTransformHelper getHelper() {
		return helper;
	}

	public void setHelper(SecurityTransformHelper helper) {
		this.helper = helper;
	}

}
