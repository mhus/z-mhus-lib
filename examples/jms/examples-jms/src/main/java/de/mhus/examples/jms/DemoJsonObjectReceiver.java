package de.mhus.examples.jms;

import java.util.Arrays;

import javax.jms.JMSException;
import javax.jms.Message;

import org.codehaus.jackson.JsonNode;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.MThread;
import de.mhus.lib.jms.JmsConnection;
import de.mhus.lib.jms.RequestResult;
import de.mhus.lib.jms.ServerJms;
import de.mhus.lib.jms.ServerJson;
import de.mhus.lib.jms.ServerJsonObject;

public class DemoJsonObjectReceiver {

	public static void main(String[] args) throws JMSException {

		String url = "tcp://localhost:61613";
		String user = "admin";
		String password = "password";
		
		JmsConnection con = new JmsConnection(url, user, password);

		ServerJsonObject server = new ServerJsonObject(con.createQueue("mike")) {

			@Override
			public void receivedOneWay(IProperties properties, Object... obj) {
				System.out.println("Received: " + properties + " " + Arrays.deepToString(obj) );
			}

			@Override
			public RequestResult<Object> received(IProperties properties,
					Object... obj) {
				System.out.println("Received: " + properties + " " + Arrays.deepToString(obj) );
				return new RequestResult<Object>(new Dummy(),properties);
			}
			
			public void receivedOneWay(IProperties properties, JsonNode node) {
				System.out.println("--- JSON: " + node);
				super.receivedOneWay(properties, node);
			}

			public RequestResult<JsonNode> received(IProperties properties, JsonNode node) {
				System.out.println("--- JSON: " + node);
				return super.received(properties,node);
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
