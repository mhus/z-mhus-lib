package de.mhus.lib.core.jms;

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

				BytesMessage answer = getDestination().getConnection().getSession().createBytesMessage();
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
