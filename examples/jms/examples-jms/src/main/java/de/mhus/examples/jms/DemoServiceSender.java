package de.mhus.examples.jms;

import java.io.IOException;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonProcessingException;

import de.mhus.lib.jms.ClientService;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.WebServiceDescriptor;

public class DemoServiceSender {

	public static void main(String[] args) throws JMSException, JsonProcessingException, IOException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		WebServiceDescriptor desc = new WebServiceDescriptor(DummyService.class);
		ClientService<DummyService> client = new ClientService<DummyService>(con.createQueue("mike"), desc);

		client.open();
		
		DummyService service = client.getClientProxy();
		
		service.dummyOneWay(new Dummy());
		service.dummySimple(new Dummy());
		service.dummyResult(new Dummy());
		try {
			service.dummyException();
		} catch (IOException e) {
			e.printStackTrace();
		}
		con.close();
	}

}
