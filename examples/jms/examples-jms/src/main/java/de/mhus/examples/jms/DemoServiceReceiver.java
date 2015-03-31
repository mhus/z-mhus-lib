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
import de.mhus.lib.jms.ServerService;
import de.mhus.lib.jms.ServiceDescriptor;
import de.mhus.lib.jms.WebServiceDescriptor;

public class DemoServiceReceiver {


	public static void main(String[] args) throws JMSException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		DummyServiceImpl service = new DummyServiceImpl();
		WebServiceDescriptor descriptor = new WebServiceDescriptor(service);
		ServerService server = new ServerService(con.createQueue("mike"), descriptor);
		
		server.open();
		
		while(true) {
			try {
				con.doChannelBeat();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			MThread.sleep(10000);
		}
	}

}
