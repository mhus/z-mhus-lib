/**
 * Copyright 2018 Mike Hummel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.lib.jms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MJson;

public class ClientJson extends ClientJms {
	
	public ClientJson(JmsDestination dest) {
		super(dest);
	}

	public void sendJsonOneWay(IProperties prop, JsonNode json) throws JMSException, IOException {
		
		ByteArrayOutputStream w = new ByteArrayOutputStream();
		try {
			MJson.save(json, w);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
		
		open();
		BytesMessage msg = getJmsDestination().getConnection().createBytesMessage();
		MJms.setProperties(prop, msg);
		msg.writeBytes(w.toByteArray());
		sendJmsOneWay(msg);
	}
	
	public RequestResult<JsonNode> sendJson(IProperties prop, JsonNode json) throws IOException, JMSException {
		
		ByteArrayOutputStream w = new ByteArrayOutputStream();
		try {
			MJson.save(json, w);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
		
		open();
		BytesMessage msg = getJmsDestination().getConnection().createBytesMessage();
		
		MJms.setProperties(prop, msg);
		msg.writeBytes(w.toByteArray());
		Message answer = sendJms(msg);
		if (answer instanceof BytesMessage) {
			BytesMessage ba = (BytesMessage)answer;
			int len = (int) ba.getBodyLength();
			byte[] b = new byte[len];
			ba.readBytes(b);
			ByteArrayInputStream r = new ByteArrayInputStream(b);
			JsonNode ja = MJson.load(r);
			IProperties p = MJms.getProperties(answer);
			return new RequestResult<JsonNode>(ja,p);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public RequestResult<JsonNode>[] sendJsonBroadcast(IProperties prop, JsonNode json) throws IOException, JMSException {
		
		ByteArrayOutputStream w = new ByteArrayOutputStream();
		try {
			MJson.save(json, w);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
		
		open();
		BytesMessage msg = getJmsDestination().getConnection().createBytesMessage();
		
		MJms.setProperties(prop, msg);
		msg.writeBytes(w.toByteArray());
		Message[] answers = sendJmsBroadcast(msg);

		LinkedList<RequestResult<JsonNode>> out = new LinkedList<>();

		for (Message answer : answers) {
			if (answer instanceof BytesMessage) {
				BytesMessage ba = (BytesMessage)answer;
				int len = (int) ba.getBodyLength();
				byte[] b = new byte[len];
				ba.readBytes(b);
				ByteArrayInputStream r = new ByteArrayInputStream(b);
				JsonNode ja = MJson.load(r);
				IProperties p = MJms.getProperties(answer);
				out.add(new RequestResult<JsonNode>(ja,p));
			}
		}
		return out.toArray(new RequestResult[out.size()] );
	}
	
}
