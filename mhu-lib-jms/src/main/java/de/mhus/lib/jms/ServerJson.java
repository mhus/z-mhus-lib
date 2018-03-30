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

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MJson;

public abstract class ServerJson extends ServerJms {

	public ServerJson(JmsDestination dest) {
		super(dest);
	}

	public abstract void receivedOneWay(IProperties properties, JsonNode node);

	public abstract RequestResult<JsonNode> received(IProperties properties, JsonNode node);

	@Override
	public void receivedOneWay(Message msg) throws JMSException {
		if (msg instanceof BytesMessage) {
			BytesMessage ba = (BytesMessage)msg;
			int len = (int) ba.getBodyLength();
			byte[] b = new byte[len];
			ba.readBytes(b);
			ByteArrayInputStream r = new ByteArrayInputStream(b);
			IProperties properties = MJms.getProperties(msg);
			try {
				JsonNode ja = MJson.load(r);
				receivedOneWay(properties, ja);
			} catch (Exception e) {
				log().w(e);
			}
			return;
		}

	}

	@Override
	public Message received(Message msg) throws JMSException {
		if (msg instanceof BytesMessage) {
			BytesMessage ba = (BytesMessage)msg;
			int len = (int) ba.getBodyLength();
			byte[] b = new byte[len];
			ba.readBytes(b);
			ByteArrayInputStream r = new ByteArrayInputStream(b);
			IProperties properties = MJms.getProperties(msg);
			try {
				JsonNode ja = MJson.load(r);
				RequestResult<JsonNode> j = received(properties, ja);
				if (j == null) return null;
				
				ByteArrayOutputStream w = new ByteArrayOutputStream();
				try {
					MJson.save(j.getResult(), w);
				} catch (IOException e) {
					throw e;
				} catch (Exception e) {
					throw new IOException(e);
				}

				BytesMessage answer = getJmsDestination().getConnection().createBytesMessage();
				MJms.setProperties(j.getProperties(), answer);
				answer.writeBytes(w.toByteArray());
				
				return answer;
				
			} catch (Exception e) {
				log().w(e);
			}
		}

		return null;
	}

}
