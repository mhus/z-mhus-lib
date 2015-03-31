package de.mhus.examples.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MThread;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.RequestResult;
import de.mhus.lib.jms.ServerJms;
import de.mhus.lib.jms.ServerJson;

public class DemoJsonReceiver {

	public static void main(String[] args) throws JMSException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		ServerJson server = new ServerJson(con.createQueue("mike")) {

			@Override
			public void receivedOneWay(IProperties p, JsonNode node) {
				System.out.println("Received: " + p + " " + node);
			}

			@Override
			public RequestResult<JsonNode> received(IProperties p, JsonNode node) {
				System.out.println("Received: " + p + " " + node);
				return null;
			}
			
		};
		
		
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
