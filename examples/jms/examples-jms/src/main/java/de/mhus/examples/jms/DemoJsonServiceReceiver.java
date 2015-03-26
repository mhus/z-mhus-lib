package de.mhus.examples.jms;

import java.util.Arrays;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MThread;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.JmsDestination;
import de.mhus.lib.jms.RequestResult;
import de.mhus.lib.jms.ServerJsonObject;
import de.mhus.lib.jms.ServerJsonService;
import de.mhus.lib.jms.ServiceDescriptor;
import de.mhus.lib.jms.WebServiceDescriptor;

public class DemoJsonServiceReceiver {


	public static void main(String[] args) throws JMSException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		DummyServiceImpl service = new DummyServiceImpl();
		WebServiceDescriptor descriptor = new WebServiceDescriptor(service);
		ServerJsonService server = new ServerJsonService(con.createQueue("mike"), descriptor) {
			
			public void receivedOneWay(IProperties properties, JsonNode node) {
				System.out.println("--- JSON: " + properties + ":" + node);
				super.receivedOneWay(properties, node);
			}

			public RequestResult<JsonNode> received(IProperties properties, JsonNode node) {
				System.out.println("--- JSON: " + properties + ":" + node);
				RequestResult<JsonNode> res = super.received(properties,node);
				if (res == null)
					System.out.println("--- RES: null");
				else
					System.out.println("--- RES: " + res.getProperties() + ":" + res.getResult());
				return res;
			}
			
		};
		
		
		while(true) {
			try {
				con.doBaseBeat();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			MThread.sleep(10000);
		}
	}

}
