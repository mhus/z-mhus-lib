package de.mhus.examples.jms;

import java.io.IOException;
import java.io.StringReader;

import javax.jms.JMSException;
import javax.jms.Message;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;

import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MThread;
import de.mhus.lib.jms.ClientJson;
import de.mhus.lib.jms.ClientJsonObject;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.RequestResult;
import de.mhus.lib.jms.ServerJms;
import de.mhus.lib.jms.ServerJson;

public class DemoJsonObjectSender {

	public static void main(String[] args) throws JMSException, JsonProcessingException, IOException, IllegalAccessException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		ClientJsonObject client = new ClientJsonObject(con.createQueue("mike"));
		Dummy dummy = null;
		dummy = new Dummy();
		System.out.println("Send: " + dummy);
		client.sendObjectOneWay(new MProperties("function","put"), dummy);
		RequestResult<Object> res = client.sendObject(new MProperties("function","get"), dummy);
		System.out.println("--- Result: " + res);
		
		con.close();
	}

}
