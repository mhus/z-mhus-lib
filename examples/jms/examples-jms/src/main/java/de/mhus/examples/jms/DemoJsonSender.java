package de.mhus.examples.jms;

import java.io.IOException;
import java.io.StringReader;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.jms.ClientJson;
import de.mhus.lib.jms.JmsConnection;

public class DemoJsonSender {

	public static void main(String[] args) throws JMSException, JsonProcessingException, IOException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		ClientJson client = new ClientJson(con.createQueue("mike"));
		
		
		JsonNode json = MJson.load(new StringReader("{\"a\":\"b\"}"));
		client.sendJsonOneWay(new MProperties("function","put"), json);
		client.sendJson(new MProperties("function","get"), json);
		
		con.close();
	}

}
