package de.mhus.examples.jms;

import java.io.IOException;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonProcessingException;

import de.mhus.lib.jms.ClientJsonObject;
import de.mhus.lib.jms.JmsConnection;

public class DemoJsonServiceMyHandSender {

	public static void main(String[] args) throws JMSException, JsonProcessingException, IOException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		ClientJsonObject client = new ClientJsonObject(con.createQueue("mike"));
		
		DummyServiceSender service = new DummyServiceSender(client);
		
		service.dummyOneWay(new Dummy());
		service.dummySimple(new Dummy());
		service.dummyResult(new Dummy());
		
		con.close();
	}

}
