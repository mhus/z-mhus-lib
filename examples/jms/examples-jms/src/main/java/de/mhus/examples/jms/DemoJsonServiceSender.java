package de.mhus.examples.jms;

import java.io.IOException;
import java.io.StringReader;

import javax.jms.JMSException;
import javax.jms.Message;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MJson;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.MThread;
import de.mhus.lib.jms.ClientJson;
import de.mhus.lib.jms.ClientJsonObject;
import de.mhus.lib.jms.ClientJsonService;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.RequestResult;
import de.mhus.lib.jms.ServerJms;
import de.mhus.lib.jms.ServerJson;
import de.mhus.lib.jms.WebServiceDescriptor;

public class DemoJsonServiceSender {

	public static void main(String[] args) throws JMSException, JsonProcessingException, IOException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		WebServiceDescriptor desc = new WebServiceDescriptor(DummyService.class);
		ClientJsonService<DummyService> client = new ClientJsonService<DummyService>(con.createQueue("mike"), desc) {

			public void sendJsonOneWay(IProperties prop, JsonNode json) throws JMSException, IOException {
				System.out.println("--- JSON: " + prop + ":" + json);
				super.sendJsonOneWay(prop, json);
			}
			public RequestResult<JsonNode> sendJson(IProperties prop, JsonNode json) throws IOException, JMSException {
				System.out.println("--- JSON: " + prop + ":" + json);
				RequestResult<JsonNode> res = super.sendJson(prop, json);
				if (res == null)
					System.out.println("--- RES: null");
				else
					System.out.println("--- RES: " + res.getProperties() + ":" + res.getResult());
				return res;
			}

		};

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
